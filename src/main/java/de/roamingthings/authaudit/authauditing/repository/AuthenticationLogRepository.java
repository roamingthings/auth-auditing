package de.roamingthings.authaudit.authauditing.repository;

import de.roamingthings.authaudit.authauditing.domain.AuthenticationLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/12
 */
public interface AuthenticationLogRepository extends JpaRepository<AuthenticationLog, Long> {
    AuthenticationLog findByUserId(Long userId);
}
