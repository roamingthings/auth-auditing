package de.roamingthings.authaudit.authauditing.service;

import de.roamingthings.authaudit.authauditing.domain.AuthenticationLog;
import de.roamingthings.authaudit.authauditing.repository.AuthenticationLogDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/16
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthenticationLogServiceImplTest {
    @Mock
    private AuthenticationLogDao authenticationLogDao;

    @Test
    public void should_calculate_series_of_4_unsuccessful_events() throws Exception {
        List<AuthenticationLog> authenticationLogList = asList(
                authenticationLogForEventClass(AuthenticationFailureBadCredentialsEvent.class, false),
                authenticationLogForEventClass(AuthenticationFailureBadCredentialsEvent.class, false),
                authenticationLogForEventClass(AuthenticationFailureBadCredentialsEvent.class, false),
                authenticationLogForEventClass(AuthenticationFailureBadCredentialsEvent.class, false),
                authenticationLogForEventClass(AuthenticationSuccessEvent.class, true)
        );

        assertUnsuccessfulSeriesSizeForAuthenticationLogList(authenticationLogList, 4);
    }

    @Test
    public void should_calculate_series_of_5_unsuccessful_events() throws Exception {
        List<AuthenticationLog> authenticationLogList = asList(
                authenticationLogForEventClass(AuthenticationFailureBadCredentialsEvent.class, false),
                authenticationLogForEventClass(AuthenticationFailureBadCredentialsEvent.class, false),
                authenticationLogForEventClass(AuthenticationFailureBadCredentialsEvent.class, false),
                authenticationLogForEventClass(AuthenticationFailureBadCredentialsEvent.class, false),
                authenticationLogForEventClass(AuthenticationFailureBadCredentialsEvent.class, false)
        );

        assertUnsuccessfulSeriesSizeForAuthenticationLogList(authenticationLogList, 5);
    }

    @Test
    public void should_calculate_series_of_no_unsuccessful_events() throws Exception {
        List<AuthenticationLog> authenticationLogList = asList(
                authenticationLogForEventClass(AuthenticationSuccessEvent.class, true),
                authenticationLogForEventClass(AuthenticationFailureBadCredentialsEvent.class, false),
                authenticationLogForEventClass(AuthenticationSuccessEvent.class, true),
                authenticationLogForEventClass(AuthenticationFailureBadCredentialsEvent.class, false),
                authenticationLogForEventClass(AuthenticationFailureBadCredentialsEvent.class, false),
                authenticationLogForEventClass(AuthenticationFailureBadCredentialsEvent.class, false),
                authenticationLogForEventClass(AuthenticationSuccessEvent.class, true)
        );

        assertUnsuccessfulSeriesSizeForAuthenticationLogList(authenticationLogList, 0);
    }

    private void assertUnsuccessfulSeriesSizeForAuthenticationLogList(List<AuthenticationLog> authenticationLogList, int expectedSeriesSize) {
        when(authenticationLogDao.findByUserIdOrderedByIncidentTimestampDesc(anyLong(), anyInt())).thenReturn(authenticationLogList);
        AuthenticationLogServiceImpl authenticationLogService = new AuthenticationLogServiceImpl(Clock.systemDefaultZone(), authenticationLogDao);

        final int seriesSize = authenticationLogService.unsuccessfulAuthenticationSeriesSize(1L, authenticationLogList.size());
        assertThat(seriesSize, is(expectedSeriesSize));
    }

    private AuthenticationLog authenticationLogForEventClass(Class<? extends AbstractAuthenticationEvent> eventClass, boolean authenticated) {
        return AuthenticationLog.of(1L, "principal", Instant.now(), eventClass, authenticated);
    }

}