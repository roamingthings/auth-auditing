package de.roamingthings.authaudit.authauditing.service;

import de.roamingthings.authaudit.authauditing.domain.AuthenticationEventType;
import de.roamingthings.authaudit.authauditing.domain.AuthenticationLog;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/12
 */
@Component
public class AuthenticationLogDaoImpl implements AuthenticationLogDao {
    private final JdbcTemplate jdbcTemplate;

    public AuthenticationLogDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createAuthenticationLog(AuthenticationLog authenticationLog) {
        jdbcTemplate.update(
                "INSERT INTO authentication_log (user_id, principal, incident_timestamp, authentication_event_type) VALUES (?, ?, ?, ?)",
                authenticationLog.getUserId(),
                authenticationLog.getPrincipal(),
                new Timestamp(authenticationLog.getIncidentTimestamp().toEpochMilli()),
                authenticationLog.getAuthenticationEventType().name());
    }

    @Override
    public List<AuthenticationLog> findAllByUserId(Long userId) {
        return jdbcTemplate.query(
                "SELECT user_id, principal, incident_timestamp, authentication_event_type FROM authentication_log WHERE user_id=?",
                new Object[]{userId},
                (resultSet, rowNum) -> {
                    AuthenticationLog authenticationLog = new AuthenticationLog(
                            resultSet.getLong(1),
                            resultSet.getString(2),
                            resultSet.getTimestamp(3).toInstant(),
                            AuthenticationEventType.valueOf(resultSet.getString(4))
                    );

                    return authenticationLog;
                });
    }
}
