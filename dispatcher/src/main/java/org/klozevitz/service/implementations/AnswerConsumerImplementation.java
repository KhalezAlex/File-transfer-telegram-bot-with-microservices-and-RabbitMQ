package org.klozevitz.service.implementations;

import lombok.RequiredArgsConstructor;
import org.klozevitz.controller.UpdateProcessor;
import org.klozevitz.service.interfaces.AnswerConsumer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.klozevitz.model.RabbitQueue.ANSWER_MESSAGE;

@Service
@RequiredArgsConstructor
public class AnswerConsumerImplementation implements AnswerConsumer {
    private final UpdateProcessor updateProcessor;

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consume(SendMessage sendMessage) {
        updateProcessor.setView(sendMessage);
    }
}
