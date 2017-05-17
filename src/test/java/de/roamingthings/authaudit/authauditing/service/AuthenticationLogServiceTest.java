package de.roamingthings.authaudit.authauditing.service;

import de.roamingthings.authaudit.authauditing.domain.AuthenticationLog;
import de.roamingthings.authaudit.authauditing.repository.AuthenticationLogDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;

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
        final AuthenticationSuccessEvent authenticationEventMock = new AuthenticationSuccessEvent(mock(Authentication.class));

        AuthenticationLogService authenticationLogService = new AuthenticationLogServiceImpl(Clock.systemDefaultZone(), authenticationLogDao);

        authenticationLogService.createAuthenticationLogEntryForUserOfType(userId, principal, authenticationEventMock, true);

        ArgumentCaptor<AuthenticationLog> authenticationLogCaptor = ArgumentCaptor.forClass(AuthenticationLog.class);

        verify(authenticationLogDao, times(1)).createAuthenticationLog(authenticationLogCaptor.capture());

        final AuthenticationLog capturedAuthenticationLog = authenticationLogCaptor.getValue();
        assertThat(capturedAuthenticationLog.getUserAccountId(), is(1L));
        assertThat(capturedAuthenticationLog.getPrincipal(), is("testPrincipal"));
        assertThat(capturedAuthenticationLog.getAuthenticationEventType(), is("org.springframework.security.authentication.event.AuthenticationSuccessEvent"));
        assertThat(capturedAuthenticationLog.isAuthenticated(), is(true));
    }
}