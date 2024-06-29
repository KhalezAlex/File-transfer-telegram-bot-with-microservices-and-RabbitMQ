package org.klozevitz.service.implementations;

import lombok.RequiredArgsConstructor;
import org.klozevitz.service.interfaces.ProducerService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.klozevitz.model.RabbitQueue.*;

@Service
@RequiredArgsConstructor
public class ProducerServiceImplementation implements ProducerService {
    private final RabbitTemplate rabbitTemplate;


    @Override
    public void produceAnswer(SendMessage sendMessage) {
        rabbitTemplate.convertAndSend(ANSWER_MESSAGE, sendMessage);
    }
}
