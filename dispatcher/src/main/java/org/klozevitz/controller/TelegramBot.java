package org.klozevitz.controller;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Component
@Log4j
public class TelegramBot extends TelegramWebhookBot {
    @Getter
    @Value("${bot.username}")
    private String botUsername;
    @Getter
    @Value("${bot.token}")
    private String botToken;
    @Getter
    @Value("${bot.url}")
    private String botUrl;
    private final UpdateProcessor updateProcessor;

    public TelegramBot(UpdateProcessor updateProcessor) {
        this.updateProcessor = updateProcessor;
    }

    @PostConstruct
    public void init() {
        updateProcessor.registerBot(this);
        try {
            var webHook = SetWebhook.builder()
                    .url(botUrl)
                    .build();
            this.setWebhook(webHook);
        } catch (TelegramApiException e) {
            log.error(e);
        }
    }

    /**
     * result address will be smth like <bot.url> + </callback> + </update>
     * example: https://lol.com/callback/update
     * */

    @Override
    public String getBotPath() {
        return "/update";
    }

    public void sendAnswer(SendMessage message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(e);
            }
        }
    }

    /**
     * will not need at this moment
     * */
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return null;
    }
}
