package org.klozevitz.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.model.entity.ApplicationDocument;
import org.klozevitz.model.entity.ApplicationPhoto;
import org.klozevitz.model.entity.BinaryContent;
import org.klozevitz.service.interfaces.FileService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
@Log4j
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @RequestMapping(method = RequestMethod.GET, value = "/get-doc")
    public ResponseEntity<?> getDoc(@RequestParam("id") String id) {
        //TODO добавить для формирования badRequest ControllerAdvice
        ApplicationDocument document = fileService.getDocument(id);
        if (document == null) {
            return ResponseEntity.badRequest().build();
        }

        BinaryContent binaryContent = document.getBinaryContent();
        FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);
        if (fileSystemResource == null) {
            // документ был найден в базе, но, по какой-то причине, мы не смогли его вернуть
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok()
                .contentType(
                        MediaType.parseMediaType(
                                document.getMimeType()
                        )
                )
                .header(
                        "Content-disposition",
                        "attachment; filename=" + document.getDocName()
                )
                .body(fileSystemResource);
    }

    //TODO разобраться с тем, почему такое мелкое фото из базы приходит
    @RequestMapping(method = RequestMethod.GET, value = "/get-photo")
    public ResponseEntity<?> getPhoto(@RequestParam("id") String id) {
        //TODO добавить для формирования badRequest ControllerAdvice
        ApplicationPhoto photo = fileService.getPhoto(id);
        if (photo == null) {
            return ResponseEntity.badRequest().build();
        }

        BinaryContent binaryContent = photo.getBinaryContent();
        FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);
        if (fileSystemResource == null) {
            // документ был найден в базе, но, по какой-то причине, мы не смогли его вернуть
            return ResponseEntity.internalServerError().build();
        }

        // телеграмм для фото не хранит название файла, поэтому эту инфу убираем
        // из Content-disposition
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header("Content-disposition", "attachment;")
                .body(fileSystemResource);

    }
}
