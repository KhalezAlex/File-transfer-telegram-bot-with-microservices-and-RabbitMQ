package org.klozevitz.service.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import org.klozevitz.CryptoTool;
import org.klozevitz.dao.ApplicationDocumentRepository;
import org.klozevitz.dao.ApplicationPhotoRepository;
import org.klozevitz.dao.BinaryContentRepository;
import org.klozevitz.entity.ApplicationDocument;
import org.klozevitz.entity.ApplicationPhoto;
import org.klozevitz.entity.BinaryContent;
import org.klozevitz.exceptions.UploadFileException;
import org.klozevitz.service.enums.LinkType;
import org.klozevitz.service.interfaces.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

@Log4j
@Service
@RequiredArgsConstructor
public class FileServiceImplementation implements FileService {
    @Value("${fileService.token}")
    private String token;
    @Value("${fileService.service.file_info.url}")
    private String fileInfoUrl;
    @Value("${fileService.service.file_storage.url}")
    private String fileStorageUrl;
    @Value("${link.address}")
    private String linkAddress;
    private final ApplicationDocumentRepository appDocRepo;
    private final ApplicationPhotoRepository appPhotoRepo;
    private final BinaryContentRepository binaryContentRepository;
    private final CryptoTool cryptoTool;

    /**
     * filePath получается путем навигации по дереву вложенных json-ключей
     */
    @Override
    public ApplicationDocument processDoc(Message telegramMessage) {
        Document telegramDoc = telegramMessage.getDocument();
        String fileId = telegramDoc.getFileId();
        ResponseEntity<String> response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            BinaryContent persistentBinaryContent = getPersistentBinaryContent(response);

            ApplicationDocument transientAppDoc = buildTransientAppDoc(telegramDoc, persistentBinaryContent);
            return appDocRepo.save(transientAppDoc);
        } else {
            throw new UploadFileException("Bad response from telegram service: " + response);
        }
    }

    @Override
    public ApplicationPhoto processPhoto(Message telegramMessage) {
        int photoSizeAmount = telegramMessage.getPhoto().size();
        int photoIndex = photoSizeAmount > 0 ? telegramMessage.getPhoto().size() - 1 : 0;
        PhotoSize telegramPhoto = telegramMessage.getPhoto().get(photoIndex);
        String fileId = telegramPhoto.getFileId();
        ResponseEntity<String> response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            BinaryContent persistentBinaryContent = getPersistentBinaryContent(response);
            ApplicationPhoto transientAppPhoto = buildTransientAppPhoto(telegramPhoto, persistentBinaryContent);
            return appPhotoRepo.save(transientAppPhoto);
        } else {
            throw new UploadFileException("Bad response from telegram service: " + response);
        }
    }

    private BinaryContent getPersistentBinaryContent(ResponseEntity<String> response) {
        String filePath = filePathFromResponseEntity(response);
        byte[] fileAsByteArray = downloadFile(filePath);
        BinaryContent transientBinaryContent = BinaryContent.builder()
                .fileAsByteArray(fileAsByteArray)
                .build();
        return binaryContentRepository.save(transientBinaryContent);
    }

    private String filePathFromResponseEntity(ResponseEntity<String> response) {
        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.getBody()));
        return String.valueOf(jsonObject
                .getJSONObject("result")
                .getString("file_path"));
    }

    private ResponseEntity<String> getFilePath(String fileId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);
        return restTemplate.exchange(
                fileInfoUrl,
                HttpMethod.GET,
                request,
                String.class,
                token,
                fileId
        );
    }

    private byte[] downloadFile(String filePath) {
        String fullUrl = fileStorageUrl
                .replace("{token}", token)
                .replace("{filePath}", filePath);
        URL urlObj;
        try {
            urlObj = new URL(fullUrl);
        } catch (MalformedURLException e) {
            throw new UploadFileException(e);
        }

        //TODO можно оптимизировать (про разбиение файла,
        // чтобы не хранить в оперативке большие файлы)
        try (InputStream is = urlObj.openStream()) {
            return is.readAllBytes();
        } catch (IOException e) {
            throw new UploadFileException(urlObj.toExternalForm(), e);
        }
    }

    private ApplicationDocument buildTransientAppDoc(Document telegramDoc, BinaryContent persistentBinaryContent) {
        return ApplicationDocument.builder()
                .telegramFileId(telegramDoc.getFileId())
                .docName(telegramDoc.getFileName())
                .binaryContent(persistentBinaryContent)
                .mimeType(telegramDoc.getMimeType())
                .fileSize(telegramDoc.getFileSize())
                .build();
    }

    private ApplicationPhoto buildTransientAppPhoto(PhotoSize telegramPhoto, BinaryContent persistentBinaryContent) {
        return ApplicationPhoto.builder()
                .telegramFileId(telegramPhoto.getFileId())
                .binaryContent(persistentBinaryContent)
                .fileSize(telegramPhoto.getFileSize())
                .build();
    }

    @Override
    public String generateLink(Long docId, LinkType linkType) {
        String hash = cryptoTool.hashOf(docId);
        return "http://" + linkAddress + "/" + linkType + "?id=" + hash;
    }
}
