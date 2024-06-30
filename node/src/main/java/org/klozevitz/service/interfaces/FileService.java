package org.klozevitz.service.interfaces;

import org.klozevitz.entity.ApplicationDocument;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    ApplicationDocument processDoc(Message externalMessage);
}
