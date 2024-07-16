package org.klozevitz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequiredArgsConstructor
public class WebHookController {
    private final UpdateProcessor updateProcessor;

    /**
     * if we want to use 5.x version or earlier, we don't use </callback> part in request url
     *
     * We can make controller-advice for custom responses when errors occur
     *
     * we should remember that if telegram receives 500 response it will send this message again and again
     * */
    @RequestMapping(value = "/callback/update", method = RequestMethod.POST)
    public ResponseEntity<?> onUpdateReceived(@RequestBody Update update) {
        updateProcessor.processUpdate(update);
        return ResponseEntity.ok().build();
    }
}
