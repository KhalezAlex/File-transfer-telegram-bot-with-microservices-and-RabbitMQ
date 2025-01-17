package org.klozevitz.service.interfaces;

import org.klozevitz.model.entity.ApplicationDocument;
import org.klozevitz.model.entity.ApplicationPhoto;
import org.klozevitz.service.enums.LinkType;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    ApplicationDocument processDoc(Message telegramMessage);
    ApplicationPhoto processPhoto(Message telegramMessage);
    String generateLink(Long docId, LinkType linkType);
}
