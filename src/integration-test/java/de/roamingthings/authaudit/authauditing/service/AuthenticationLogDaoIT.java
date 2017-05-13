package de.roamingthings.authaudit.authauditing.service;

import de.roamingthings.authaudit.authauditing.domain.AuthenticationEventType;
import de.roamingthings.authaudit.authauditing.domain.AuthenticationLog;
import de.roamingthings.authaudit.authauditing.repository.AuthenticationLogDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("it")
public class AuthenticationLogDaoIT {

    @Autowired
    private AuthenticationLogDao authenticationLogDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        jdbcTemplate.update("DELETE FROM authentication_log");
    }

    @Test
    public void should_save_new_authentication_log() throws Exception {
        AuthenticationLog authenticationLog = new AuthenticationLog(1L, "testPrincipal", Instant.now(), AuthenticationEventType.LOGGED_IN_SUCCESSFUL);

        authenticationLogDao.createAuthenticationLog(authenticationLog);

        final Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM authentication_log WHERE user_id=?", new Object[]{1L}, Integer.class);
        assertThat(count, is(1));
    }

    @Test
    public void should_create_and_read_back_authentication_log() throws Exception {
        Stream.of(
                new AuthenticationLog(1L, "testPrincipal", Instant.now(), AuthenticationEventType.LOGGED_IN_SUCCESSFUL),
                new AuthenticationLog(1L, "testPrincipal", Instant.now(), AuthenticationEventType.LOGGED_OUT),
                new AuthenticationLog(2L, "principalTest", Instant.now(), AuthenticationEventType.ACCOUNT_DISABLED)
        )
                .forEach(authenticationLogDao::createAuthenticationLog);

        final List<AuthenticationLog> authenticationLogList = authenticationLogDao.findAllByUserId(1L);
        assertThat(authenticationLogList, hasSize(2));
        assertThat(authenticationLogList, hasItems(
                allOf(hasProperty("principal", is("testPrincipal")),
                    hasProperty("authenticationEventType", is(AuthenticationEventType.LOGGED_IN_SUCCESSFUL))),
                allOf(hasProperty("principal", is("testPrincipal")),
                    hasProperty("authenticationEventType", is(AuthenticationEventType.LOGGED_OUT)))
        ));
    }

}