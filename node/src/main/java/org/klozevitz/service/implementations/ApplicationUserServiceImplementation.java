package org.klozevitz.service.implementations;

import lombok.RequiredArgsConstructor;
import org.klozevitz.dao.ApplicationUserRepository;
import org.klozevitz.entity.ApplicationUser;
import org.klozevitz.service.interfaces.ApplicationUserService;
import org.klozevitz.utils.CryptoTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationUserServiceImplementation implements ApplicationUserService {
    private final ApplicationUserRepository applicationUserRepository;
    private final CryptoTool cryptoTool;
    @Value("${fileService.service.mail.url}")
    private String mailServiceUrl;

    @Override
    public String registerUser(ApplicationUser applicationUser) {
        return null;
    }

    @Override
    public String setEmail(ApplicationUser applicationUser, String email) {
        return null;
    }
}
