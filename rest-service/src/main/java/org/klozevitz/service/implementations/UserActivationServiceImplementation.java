package org.klozevitz.service.implementations;

import lombok.RequiredArgsConstructor;
import org.klozevitz.model.repositories.ApplicationUserRepository;
import org.klozevitz.model.entity.ApplicationUser;
import org.klozevitz.service.interfaces.UserActivationService;
import org.klozevitz.utils.CryptoTool;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserActivationServiceImplementation implements UserActivationService {
    private final ApplicationUserRepository appUserRepo;
    private final CryptoTool cryptoTool;

    @Override
    public boolean activation(String cryptoUserId) {
        var userId = cryptoTool.idOf(cryptoUserId);
        var optionalUser = appUserRepo.findById(userId);
        if (optionalUser.isPresent()) {
            ApplicationUser user = optionalUser.get();
            user.setIsActive(true);
            appUserRepo.save(user);
            return true;
        }
        return false;
    }
}
