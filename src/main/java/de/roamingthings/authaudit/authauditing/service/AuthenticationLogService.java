package de.roamingthings.authaudit.authauditing.service;

import de.roamingthings.authaudit.authauditing.domain.AuthenticationLog;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/16
 */
public interface AuthenticationLogService {
    @Transactional
    void createAuthenticationLogEntryForUserOfType(Long userId, String principal, AbstractAuthenticationEvent authenticationEvent, boolean authenticated);

    @Transactional(readOnly = true)
    List<AuthenticationLog> authenticationLogHistoryForPrincipal(Long userId, int limit);

    @Transactional(readOnly = true)
    int unsuccessfulAuthenticationSeriesSize(Long userId, int limit);
}
