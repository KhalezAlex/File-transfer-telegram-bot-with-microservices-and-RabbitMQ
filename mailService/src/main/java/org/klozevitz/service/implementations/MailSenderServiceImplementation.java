package org.klozevitz.service.implementations;

import lombok.RequiredArgsConstructor;
import org.klozevitz.dto.MailParameters;
import org.klozevitz.service.interfaces.MailSenderService;
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
        String subject = "Account activation";
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
        String message = String.format("Click the link below to complete registration:\n%s",
                activationServiceUrl);
        return message.replace("{id}", id);
//        return String
//                .format("Click the link below to complete registration:\n%s",
//                        activationServiceUrl)
//                .replace("{id}", id);
    }
}
