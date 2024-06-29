package org.klozevitz.service.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.service.interfaces.UpdateProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@Service
@RequiredArgsConstructor
public class UpdateProducerImplementation implements UpdateProducer {
    private final RabbitTemplate rabbitTemplate;

    /**
     * логгируем только текст, поэтому, вместо текста, будет прочерк
     * */
    @Override
    public void produce(String rabbitQueue, Update update) {
        log.debug(update.getMessage().getText());
        rabbitTemplate.convertAndSend(rabbitQueue, update);
    }
}
