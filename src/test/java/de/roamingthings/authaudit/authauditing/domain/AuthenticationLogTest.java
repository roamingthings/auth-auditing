package de.roamingthings.authaudit.authauditing.domain;

import org.junit.Test;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

import java.time.Instant;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/14
 */
public class AuthenticationLogTest {
    @Test
    public void should_create_authentication_log_of_event_class() throws Exception {
        final Instant incidentTimestamp = Instant.now();
        final AuthenticationLog authenticationLog = AuthenticationLog.of(1L, "principal", incidentTimestamp, AuthenticationSuccessEvent.class, true);

        assertThat(authenticationLog.getUserId(), is(1L));
        assertThat(authenticationLog.getPrincipal(), is("principal"));
        assertThat(authenticationLog.getIncidentTimestamp(), is(incidentTimestamp));
        assertThat(authenticationLog.getAuthenticationEventType(), is("org.springframework.security.authentication.event.AuthenticationSuccessEvent"));
        assertThat(authenticationLog.isAuthenticated(), is(true));
    }

}