package de.roamingthings.authaudit.authauditing.service;

import de.roamingthings.authaudit.authauditing.domain.AuthenticationLog;

import java.util.List;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/12
 */
public interface AuthenticationLogDao {
    void createAuthenticationLog(AuthenticationLog authenticationLog);

    List<AuthenticationLog> findAllByUserId(Long userId);
}
