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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/file")
@Log4j
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @RequestMapping(method = RequestMethod.GET, value = "/get-doc")
    public void getDoc(@RequestParam("id") String id, HttpServletResponse response) {
        //TODO добавить для формирования badRequest ControllerAdvice
        ApplicationDocument document = fileService.getDocument(id);
        if (document == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        response.setContentType(MediaType.parseMediaType(document.getMimeType()).toString());
        // if we want the file to be downloaded and not to be just opened in web-browser
        response.setHeader("Content-disposition", "attachment; filename=" + document.getDocName());
        response.setStatus(HttpServletResponse.SC_OK);

        var binaryContent = document.getBinaryContent();
        try {
            var out = response.getOutputStream();
            out.write(binaryContent.getFileAsByteArray());
            out.close();
        } catch (IOException e) {
            log.error(e);
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get-photo")
    public void getPhoto(@RequestParam("id") String id, HttpServletResponse response) {
        //TODO добавить для формирования badRequest ControllerAdvice
        ApplicationPhoto photo = fileService.getPhoto(id);
        if (photo == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        response.setContentType(MediaType.IMAGE_JPEG.toString());
        // if we want the file to be downloaded and not to be just opened in web-browser
        response.setHeader("Content-disposition", "attachment:");
        response.setStatus(HttpServletResponse.SC_OK);

        var binaryContent = photo.getBinaryContent();
        try {
            var out = response.getOutputStream();
            out.write(binaryContent.getFileAsByteArray());
            out.close();
        } catch (IOException e) {
            log.error(e);
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
        }
    }
}
