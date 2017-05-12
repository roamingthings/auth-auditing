package de.roamingthings.authaudit.authauditing.service;

import de.roamingthings.authaudit.authauditing.domain.AuthenticationEventType;
import de.roamingthings.authaudit.authauditing.domain.AuthenticationLog;
import de.roamingthings.authaudit.authauditing.repository.AuthenticationLogRepository;
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
    private AuthenticationLogRepository authenticationLogRepository;

    @Test
    public void should_create_authentication_log() {
        final Long userId = 1L;
        final AuthenticationLog authenticationLogMock = mock(AuthenticationLog.class);
        when(authenticationLogRepository.save(any(AuthenticationLog.class))).thenReturn(authenticationLogMock);

        AuthenticationLogService authenticationLogService = new AuthenticationLogService(Clock.systemDefaultZone(), authenticationLogRepository);

        authenticationLogService.createAuthenticationLogEntryForUserOfType(userId, AuthenticationEventType.LOGGED_IN_SUCCESSFUL);

        ArgumentCaptor<AuthenticationLog> authenticationLogCaptor = ArgumentCaptor.forClass(AuthenticationLog.class);

        verify(authenticationLogRepository, times(1)).save(authenticationLogCaptor.capture());

        final AuthenticationLog capturedAuthenticationLog = authenticationLogCaptor.getValue();
        assertThat(capturedAuthenticationLog.getUserId(), is(1L));
    }
}