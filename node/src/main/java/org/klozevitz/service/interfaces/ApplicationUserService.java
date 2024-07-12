package org.klozevitz.service.interfaces;

import org.klozevitz.model.entity.ApplicationUser;

public interface ApplicationUserService {
    String registerUser(ApplicationUser appUser);
    String setEmail(ApplicationUser appUser, String email);
}
