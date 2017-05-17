package de.roamingthings.authaudit.authauditing.service;

import de.roamingthings.authaudit.authauditing.domain.AuthenticationLog;
import de.roamingthings.authaudit.authauditing.repository.AuthenticationLogDao;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/12
 */
@Service
public class AuthenticationLogServiceImpl implements AuthenticationLogService {
    private final Clock systemClock;
    private final AuthenticationLogDao authenticationLogDao;

    public AuthenticationLogServiceImpl(Clock systemClock, AuthenticationLogDao authenticationLogDao) {
        this.systemClock = systemClock;
        this.authenticationLogDao = authenticationLogDao;
    }

    @Override
    @Transactional
    public void createAuthenticationLogEntryForUserOfType(Long userId, String principal, AbstractAuthenticationEvent authenticationEvent, boolean authenticated) {
        AuthenticationLog authenticationLog = AuthenticationLog.of(userId, principal, Instant.now(systemClock), authenticationEvent.getClass(), authenticated);
        authenticationLogDao.createAuthenticationLog(authenticationLog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthenticationLog> authenticationLogHistoryForPrincipal(Long userId, int limit) {
        return authenticationLogDao.findByUserIdOrderedByIncidentTimestampDesc(userId, limit);
    }

    @Override
    @Transactional(readOnly = true)
    public int unsuccessfulAuthenticationSeriesSize(Long userId, int limit) {
        final List<AuthenticationLog> authenticationLogs = authenticationLogHistoryForPrincipal(userId, limit);

        OptionalInt indexOpt = IntStream.range(0, authenticationLogs.size())
                .filter(i -> AuthenticationEventTypePolicy.isSuccessFullAuthenticationEvent(authenticationLogs.get(i)))
                .findFirst();

        return indexOpt.orElse(limit);
    }
}
