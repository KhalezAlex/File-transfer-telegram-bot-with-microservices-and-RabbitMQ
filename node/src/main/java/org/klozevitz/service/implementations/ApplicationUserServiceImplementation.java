package org.klozevitz.service.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.model.repositories.ApplicationUserRepository;
import org.klozevitz.dto.MailParameters;
import org.klozevitz.model.entity.ApplicationUser;
import org.klozevitz.service.interfaces.ApplicationUserService;
import org.klozevitz.utils.CryptoTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import java.util.Optional;

import static org.klozevitz.model.entity.enums.ApplicationUserState.BASIC_STATE;
import static org.klozevitz.model.entity.enums.ApplicationUserState.WAIT_FOR_EMAIL_STATE;

@Log4j
@Service
@RequiredArgsConstructor
public class ApplicationUserServiceImplementation implements ApplicationUserService {
    private final ApplicationUserRepository appUserRepo;
    private final CryptoTool cryptoTool;
    @Value("${fileService.service.mail.url}")
    private String mailServiceUrl;

    @Override
    public String registerUser(ApplicationUser applicationUser) {
        if (applicationUser.getIsActive()) {
            return "You are already registered.";
        } else if (applicationUser.getEmail() != null) {
            return "Confirmation email has been sent to your email address.\n" +
                    "Follow the instructions in that email.";
        }
        applicationUser.setState(WAIT_FOR_EMAIL_STATE);
        appUserRepo.save(applicationUser);
        return "Please enter your email address.";
    }

    @Override
    public String setEmail(ApplicationUser applicationUser, String email) {
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException e) {
            return "Please enter the correct email address. Type \"/cancel\" to abort operation.";
        }

        Optional<ApplicationUser> userByEmail = appUserRepo.findByEmail(email);

        if (userByEmail.isEmpty()) {
            applicationUser.setEmail(email);
            applicationUser.setState(BASIC_STATE);
            applicationUser = appUserRepo.save(applicationUser);

            String cryptoUserId = cryptoTool.hashOf(applicationUser.getId());
            ResponseEntity<String> response = sendRequestToMailService(cryptoUserId, email);

            if (response.getStatusCode() != HttpStatus.OK) {
                String message = String.format("Email to %s cannot be sent.", email);
                log.error(message);
                applicationUser.setEmail(null);
                appUserRepo.save(applicationUser);
                return message;
            }
            return "Confirmation email has been sent to your email address.\n" +
                    "Follow the instructions in that email.";
        } else {
            return "This address is already in use. Choose another address. " +
                    "Type \"/cancel\" to abort operation.";
        }
    }

    private ResponseEntity<String> sendRequestToMailService(String cryptoUserId, String email) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MailParameters mailParameters = MailParameters.builder()
                .id(cryptoUserId)
                .emailTo(email)
                .build();
        var request = new HttpEntity<>(mailParameters, headers);
        return restTemplate.exchange(
                mailServiceUrl,
                HttpMethod.POST,
                request,
                String.class
        );
    }
}
