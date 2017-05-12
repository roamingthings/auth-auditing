package de.roamingthings.authaudit.authauditing.service;

import de.roamingthings.authaudit.authauditing.domain.AuthenticationEventType;
import de.roamingthings.authaudit.authauditing.domain.AuthenticationLog;
import de.roamingthings.authaudit.authauditing.repository.AuthenticationLogRepository;

import java.time.Clock;
import java.time.Instant;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/12
 */
public class AuthenticationLogService {
    private final Clock systemClock;
    private final AuthenticationLogRepository authenticationLogRepository;

    public AuthenticationLogService(Clock systemClock, AuthenticationLogRepository authenticationLogRepository) {
        this.systemClock = systemClock;
        this.authenticationLogRepository = authenticationLogRepository;
    }

    public void createAuthenticationLogEntryForUserOfType(Long userId, AuthenticationEventType authenticationEventType) {
        AuthenticationLog authenticationLog = new AuthenticationLog(userId, Instant.now(systemClock), authenticationEventType);
        authenticationLogRepository.save(authenticationLog);
    }
}
