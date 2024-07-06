package org.klozevitz.controller;

import lombok.RequiredArgsConstructor;
import org.klozevitz.dto.MailParameters;
import org.klozevitz.service.interfaces.MailSenderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {
    private final MailSenderService mailSenderService;

    //TODO 500-error- can't receive auth token from the mail server
    @PostMapping("/send")
    public ResponseEntity<?> sendActivationMail(@RequestBody MailParameters mailParameters) {
        mailSenderService.send(mailParameters);
        return ResponseEntity.ok().build();
    }
}
