package org.klozevitz.service.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import org.klozevitz.utils.CryptoTool;
import org.klozevitz.model.repositories.ApplicationDocumentRepository;
import org.klozevitz.model.repositories.ApplicationPhotoRepository;
import org.klozevitz.model.entity.ApplicationDocument;
import org.klozevitz.model.entity.ApplicationPhoto;
import org.klozevitz.model.entity.BinaryContent;
import org.klozevitz.service.interfaces.FileService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Log4j
@Service
@RequiredArgsConstructor
public class FileServiceImplementation implements FileService {
    private final ApplicationDocumentRepository appDocRepo;
    private final ApplicationPhotoRepository appPhotoRepo;
    private final CryptoTool cryptoTool;

    @Override
    public ApplicationDocument getDocument(String hash) {
        Long id = cryptoTool.idOf(hash);
        if (id == null) {
            return null;
        }
        return appDocRepo.findById(id).orElse(null);
    }

    @Override
    public ApplicationPhoto getPhoto(String hash) {
        Long id = cryptoTool.idOf(hash);
        if (id == null) {
            return null;
        }
        return appPhotoRepo.findById(id).orElse(null);
    }

    /**
     * deleteOnExit ставит файл в очередь на удаление после завершения работы программы
     * */

    @Override
    public FileSystemResource getFileSystemResource(BinaryContent binaryContent) {
        try {
            //TODO добавить генерацию случайных названий файлу
            File temp = File.createTempFile("tempFile", ".bin");
            temp.deleteOnExit();
            FileUtils.writeByteArrayToFile(temp, binaryContent.getFileAsByteArray());
            return new FileSystemResource(temp);
        } catch (IOException e) {
            log.error(e);
            return null;
        }
    }
}
