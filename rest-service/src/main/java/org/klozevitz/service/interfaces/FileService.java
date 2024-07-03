package org.klozevitz.service.interfaces;

import org.klozevitz.entity.ApplicationDocument;
import org.klozevitz.entity.ApplicationPhoto;
import org.klozevitz.entity.BinaryContent;
import org.springframework.core.io.FileSystemResource;

/**
 * id- передаем в виде строки, тк в открытом виде id передавать небезопасно-
 * будем передавать в виде хеш-строки и на стороне сервера этот хэш
 * дешифровывать обратно в число
 *
 * Временно, передаем id в открытом виде
 * */

public interface FileService {
    ApplicationDocument getDocument(String docId);
    ApplicationPhoto getPhoto(String photoId);
    /**
     * FileSystemResource- тип данных, необходимый для передачи контента в теле http-ответа
     * */
    FileSystemResource getFileSystemResource(BinaryContent binaryContent);
}
