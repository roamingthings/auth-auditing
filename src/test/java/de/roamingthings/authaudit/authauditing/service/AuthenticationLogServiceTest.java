package de.roamingthings.authaudit.authauditing.service;

import de.roamingthings.authaudit.authauditing.domain.AuthenticationEventType;
import de.roamingthings.authaudit.authauditing.domain.AuthenticationLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Clock;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/12
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthenticationLogServiceTest {

    @Mock
    private AuthenticationLogDao authenticationLogDao;

    @Test
    public void should_create_authentication_log() {
        final Long userId = 1L;
        final String principal = "testPrincipal";

        AuthenticationLogService authenticationLogService = new AuthenticationLogService(Clock.systemDefaultZone(), authenticationLogDao);

        authenticationLogService.createAuthenticationLogEntryForUserOfType(userId, principal, AuthenticationEventType.LOGGED_IN_SUCCESSFUL);

        ArgumentCaptor<AuthenticationLog> authenticationLogCaptor = ArgumentCaptor.forClass(AuthenticationLog.class);

        verify(authenticationLogDao, times(1)).createAuthenticationLog(authenticationLogCaptor.capture());

        final AuthenticationLog capturedAuthenticationLog = authenticationLogCaptor.getValue();
        assertThat(capturedAuthenticationLog.getUserId(), is(1L));
        assertThat(capturedAuthenticationLog.getPrincipal(), is("testPrincipal"));
    }
}