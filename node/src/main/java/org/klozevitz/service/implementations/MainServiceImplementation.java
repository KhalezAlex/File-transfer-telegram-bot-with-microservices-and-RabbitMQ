package org.klozevitz.service.implementations;

import lombok.RequiredArgsConstructor;
import org.klozevitz.dao.ApplicationUserRepository;
import org.klozevitz.dao.repositories.RawDataRepository;
import org.klozevitz.entity.ApplicationUser;
import org.klozevitz.entity.RawData;
import org.klozevitz.service.interfaces.MainService;
import org.klozevitz.service.interfaces.ProducerService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.klozevitz.entity.enums.AppUserState.BASIC_STATE;

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
     * */
    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);

        ApplicationUser applicationUser = findOrSaveApplicationUser(
                update
                        .getMessage()
                        .getFrom()
        );

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(
                update
                        .getMessage()
                        .getChatId()
                        .toString()
        );
        sendMessage.setText("Hello from NODE");

        producerService.produceAnswer(sendMessage);
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
    private ApplicationUser findOrSaveApplicationUser(User telegramUser) {
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
