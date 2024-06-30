package org.klozevitz.service.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.dao.ApplicationUserRepository;
import org.klozevitz.dao.repositories.RawDataRepository;
import org.klozevitz.entity.ApplicationUser;
import org.klozevitz.entity.RawData;
import org.klozevitz.entity.enums.AppUserState;
import org.klozevitz.service.interfaces.MainService;
import org.klozevitz.service.interfaces.ProducerService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.klozevitz.entity.enums.AppUserState.BASIC_STATE;
import static org.klozevitz.entity.enums.AppUserState.WAIT_FOR_EMAIL_STATE;
import static org.klozevitz.service.enums.ServiceCommands.*;

@Log4j
@Service
@RequiredArgsConstructor
public class MainServiceImplementation implements MainService {
    private final RawDataRepository rawDataRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final ProducerService producerService;

    /**
     * 1) Сохранение сообщения в базу
     * 2) Формирование ответа
     * 3) Отправка на обработку в очередь ответов
     *
     * Последняя ветка else на случай, если будет введен новый стейт,
     * но не будет добавлена его обработка
     * (или не будет перезапущен микровервис, который, в таком случае,
     * не будет ничего знать о новой ноде)
     * */
    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);

        ApplicationUser applicationUser = findOrSaveApplicationUser(update);
        AppUserState userState = applicationUser.getState();
        String text = update.getMessage().getText();
        String output = "";
        
        if (CANCEL.equals(text)) {
            output = cancelProcess(applicationUser);
        } else if (BASIC_STATE.equals(userState)) {
            output = processServiceCommand(applicationUser, text);
        } else if (WAIT_FOR_EMAIL_STATE.equals(userState)) {
            //TODO добавить обработку мейла
        } else {
            log.error("Unknown user state" + userState);
            output = "Неизвестная ошибка! Введите \"/cancel\" и попробуйте снова!";
        }

        Long chatId = update.getMessage().getChatId();
        sendAnswer(output, chatId);
    }

    @Override
    public void processDocMessage(Update update) {
        saveRawData(update);

        ApplicationUser applicationUser = findOrSaveApplicationUser(update);
        Long chatId = update.getMessage().getChatId();

        if (isNotAllowedToSendContent(chatId, applicationUser)) {
            return;
        }

        //TODO добавить сохранение документа
        String answer = "Докумен успешно загружен! Ссылка для скачивания: https://test.ru/get-doc/777";
        sendAnswer(answer, chatId);
    }

    @Override
    public void processPhotoMessage(Update update) {
        saveRawData(update);

        ApplicationUser applicationUser = findOrSaveApplicationUser(update);
        Long chatId = update.getMessage().getChatId();

        if (isNotAllowedToSendContent(chatId, applicationUser)) {
            return;
        }

        //TODO добавить сохранение фото
        String answer = "Фото успешно загружено! Ссылка для скачивания: https://test.ru/get-photo/777";
        sendAnswer(answer, chatId);
    }

    private boolean isNotAllowedToSendContent(Long chatId, ApplicationUser applicationUser) {
        String error;
        AppUserState state = applicationUser.getState();
        if (!applicationUser.getIsActive()) {
            error = "Зарегистрируйтесь или активируйте свою учетную запись для загрузки контента.";
            sendAnswer(error, chatId);
            return true;
        } else if (!BASIC_STATE.equals(state)) {
            error = "Отмените текущую команду с помощью \"/cancel\" для отправки файлов.";
            sendAnswer(error, chatId);
            return true;
        }
        return false;
    }

    private void sendAnswer(String output, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        producerService.produceAnswer(sendMessage);
    }

    private String processServiceCommand(ApplicationUser applicationUser, String command) {
        if (REGISTRATION.equals(command)) {
            //TODO добавить регистрацию
            return "Временно не доступно!";
        } else if (HELP.equals(command)) {
            return help();
        } else if (START.equals(command)) {
            return "Здравствуйте! \nЧтобы посмотреть список доступных команд, введите \"/help\"";
        } else {
            return "Неизвестная команда! \nЧтобы посмотреть список доступных команд, введите \"/help\"";
        }
    }

    private String help() {
        return "Список доступных команд:\n" +
                "/cancel - отмена выполнения текуещй команды;\n" +
                "/registration - регистрация пользователя";
    }

    private String cancelProcess(ApplicationUser appUser) {
        appUser.setState(BASIC_STATE);
        applicationUserRepository.save(appUser);
        return "Команда отменена!";
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();
        rawDataRepository.save(rawData);
    }

    /**
     * Обратить внимание, что юзер на вход подается именно ТЕЛЕГРАММОВСКИЙ
     * persistent - означает то, что объект, предположительно, есть в бд
     * transient - означает то, что объект только предстоит сохранить в бд
     */
    private ApplicationUser findOrSaveApplicationUser(Update update) {
        User telegramUser = update
                .getMessage()
                .getFrom();
        ApplicationUser persistentApplicationUser =
                applicationUserRepository
                        .findApplicationUserByTelegramUserId(telegramUser.getId());
        if (persistentApplicationUser == null) {
            ApplicationUser transientApplicationUser = ApplicationUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .username(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    //TODO изменить значение по умолчанию после добавления регистрации
                    .isActive(true)
                    .state(BASIC_STATE)
                    .build();
            return applicationUserRepository.save(transientApplicationUser);
        }
        return persistentApplicationUser;
    }
}
