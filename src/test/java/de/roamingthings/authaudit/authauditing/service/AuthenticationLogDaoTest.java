package de.roamingthings.authaudit.authauditing.service;

import de.roamingthings.authaudit.authauditing.domain.AuthenticationLog;
import de.roamingthings.authaudit.authauditing.repository.AuthenticationLogDao;
import de.roamingthings.authaudit.authauditing.repository.AuthenticationLogDaoImpl;
import de.roamingthings.authaudit.authauditing.repository.AuthenticationLogRowMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

import java.time.Instant;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/12
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthenticationLogDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Test
    public void should_save() throws Exception {
        AuthenticationLogDao authenticationLogDao = new AuthenticationLogDaoImpl(jdbcTemplate, new AuthenticationLogRowMapper());
        AuthenticationLog authenticationLog = AuthenticationLog.of(1L, "testPrincipal", Instant.now(), AuthenticationSuccessEvent.class, true);

        authenticationLogDao.createAuthenticationLog(authenticationLog);

        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object[]> parametersCaptor = ArgumentCaptor.forClass(Object[].class);

        verify(jdbcTemplate, times(1)).update(
                queryCaptor.capture(),
                parametersCaptor.capture());

        assertThat(queryCaptor.getValue(), containsString("INSERT INTO authentication_log (user_account_id, principal, incident_timestamp, authentication_event_type, authenticated) VALUES (?, ?, ?, ?, ?)"));
//        assertThat(parametersCaptor.getValue(), hasItems(authenticationLog.getUserAccountId(), authenticationLog.getPrincipal(), authenticationLog.getIncidentTimestamp(), authenticationLog.getAuthenticationEventType(), true));
    }

}