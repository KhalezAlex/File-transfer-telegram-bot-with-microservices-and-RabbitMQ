package org.klozevitz.service.implementations;

import lombok.RequiredArgsConstructor;
import org.klozevitz.dao.ApplicationUserRepository;
import org.klozevitz.entity.ApplicationUser;
import org.klozevitz.service.interfaces.UserActivationService;
import org.klozevitz.utils.CryptoTool;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserActivationServiceImplementation implements UserActivationService {
    private final ApplicationUserRepository appUserRepo;
    private final CryptoTool cryptoTool;

    @Override
    public boolean activation(String cryptoUserId) {
        long userId = cryptoTool.idOf(cryptoUserId);
        Optional<ApplicationUser> optionalUser = appUserRepo.findById(userId);
        if (optionalUser.isPresent()) {
            ApplicationUser user = optionalUser.get();
            user.setIsActive(true);
            appUserRepo.save(user);
            return true;
        }
        return false;
    }
}
