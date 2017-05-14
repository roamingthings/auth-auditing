package de.roamingthings.authaudit.authauditing.service;

import de.roamingthings.authaudit.authauditing.domain.AuthenticationLog;
import de.roamingthings.authaudit.authauditing.repository.AuthenticationLogDao;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/12
 */
@Service
public class AuthenticationLogService {
    private final Clock systemClock;
    private final AuthenticationLogDao authenticationLogDao;

    public AuthenticationLogService(Clock systemClock, AuthenticationLogDao authenticationLogDao) {
        this.systemClock = systemClock;
        this.authenticationLogDao = authenticationLogDao;
    }

    @Transactional
    public void createAuthenticationLogEntryForUserOfType(Long userId, String principal, AbstractAuthenticationEvent authenticationEvent, boolean authenticated) {
        AuthenticationLog authenticationLog = AuthenticationLog.of(userId, principal, Instant.now(systemClock), authenticationEvent.getClass(), authenticated);
        authenticationLogDao.createAuthenticationLog(authenticationLog);
    }
}
