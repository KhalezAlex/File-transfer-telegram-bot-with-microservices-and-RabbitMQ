package org.klozevitz.model.repositories;

import org.klozevitz.model.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    Optional<ApplicationUser> findByTelegramUserId(long telegramUserId);
    Optional<ApplicationUser> findById(long id);
    Optional<ApplicationUser> findByEmail(String email);
}
