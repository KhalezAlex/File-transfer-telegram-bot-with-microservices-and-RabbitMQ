package org.klozevitz.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.service.interfaces.UpdateProducer;
import org.klozevitz.util.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.model.RabbitQueue.*;

@Log4j
@Component
@RequiredArgsConstructor
public class UpdateProcessor {
    private TelegramBot telegramBot;
    private final MessageUtil messageUtil;
    private final UpdateProducer updateProducer;

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update) {
        if(update == null) {
            log.error("Received update is null");
            return;
        }

        if (update.hasMessage()) {
            distributeMessageByType(update);
        } else {
            log.error("Unsupported received message type " + update);
        }
      }

    private void distributeMessageByType(Update update) {
        Message message = update.getMessage();
        if (message.hasText()) {
            processTextMessage(update);
        } else if (message.hasDocument()) {
            processDocMessage(update);
        } else if (message.hasPhoto()) {
            processPhotoMessage(update);
        } else {
            setUnsupportedMessageTypeView(update);
        }
    }

    private void setUnsupportedMessageTypeView(Update update) {
        var sendMessage = messageUtil.generateSendMessageWithText(update,
                "Message type is not supported!");
        setView(sendMessage);
    }

    private void setFileIsReceivedView(Update update) {
        var sendMessage = messageUtil.generateSendMessageWithText(update,
                "File accepted. Proceeding...");
        setView(sendMessage);
    }

    public void setView(SendMessage sendMessage) {
        telegramBot.sendAnswer(sendMessage);
    }

    private void processPhotoMessage(Update update) {
        updateProducer.produce(PHOTO_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update);
    }

    private void processDocMessage(Update update) {
        updateProducer.produce(DOC_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update);
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }
}

