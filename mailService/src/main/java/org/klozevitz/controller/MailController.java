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

    //TODO implement controller advice- catching non-200 responses
    @PostMapping("/send")
    public ResponseEntity<?> sendActivationMail(@RequestBody MailParameters mailParameters) {
        mailSenderService.send(mailParameters);
        return ResponseEntity.ok().build();
    }
}
