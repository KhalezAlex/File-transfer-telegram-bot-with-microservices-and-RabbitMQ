package org.klozevitz.dao;

import org.klozevitz.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    ApplicationUser findApplicationUserByTelegramUserId(long telegramUserId);
}
