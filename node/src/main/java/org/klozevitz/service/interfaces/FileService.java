package org.klozevitz.service.interfaces;

import org.klozevitz.entity.ApplicationDocument;
import org.klozevitz.entity.ApplicationPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    ApplicationDocument processDoc(Message telegramMessage);
    ApplicationPhoto processPhoto(Message telegramMessage);
}
