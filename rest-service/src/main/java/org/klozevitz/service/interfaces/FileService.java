package org.klozevitz.service.interfaces;

import org.klozevitz.model.entity.ApplicationDocument;
import org.klozevitz.model.entity.ApplicationPhoto;
import org.klozevitz.model.entity.BinaryContent;
import org.springframework.core.io.FileSystemResource;

/**
 * id- передаем в виде строки, тк в открытом виде id передавать небезопасно-
 * будем передавать в виде хеш-строки и на стороне сервера этот хэш
 * дешифровывать обратно в число
 *
 * Временно, передаем id в открытом виде
 * */

public interface FileService {
    ApplicationDocument getDocument(String hash);
    ApplicationPhoto getPhoto(String hash);
    /**
     * FileSystemResource- тип данных, необходимый для передачи контента в теле http-ответа
     * */
    FileSystemResource getFileSystemResource(BinaryContent binaryContent);
}
