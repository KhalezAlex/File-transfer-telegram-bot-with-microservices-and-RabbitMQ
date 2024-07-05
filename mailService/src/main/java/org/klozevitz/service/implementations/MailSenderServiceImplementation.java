package org.klozevitz.service.implementations;

import lombok.RequiredArgsConstructor;
import org.klozevitz.dto.MailParameters;
import org.klozevitz.service.MailSenderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderServiceImplementation implements MailSenderService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String emailFrom;
    @Value("${service.activation.url}")
    private String activationServiceUrl;

    @Override
    public void send(MailParameters mailParameters) {
        String subject = "Активация учетной записи";
        String messageBody = getActivationMessageBody(mailParameters.getId());
        String emailTo = mailParameters.getEmailTo();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(messageBody);

        javaMailSender.send(mailMessage);
    }

    private String getActivationMessageBody(String id) {
        var message = String.format("Для завершения регистрации перейдите по ссылке:\n%s",
                activationServiceUrl);
        return message.replace("{id}", id);
//        return String
//                .format("Для завершения регистрации перейдите по ссылке:\n%s",
//                        activationServiceUrl)
//                .replace("{id}", id);
    }
}
