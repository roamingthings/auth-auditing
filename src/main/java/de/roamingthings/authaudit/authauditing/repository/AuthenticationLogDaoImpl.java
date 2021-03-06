package de.roamingthings.authaudit.authauditing.repository;

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
    private final AuthenticationLogRowMapper authenticationLogRowMapper;

    public AuthenticationLogDaoImpl(JdbcTemplate jdbcTemplate, AuthenticationLogRowMapper authenticationLogRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.authenticationLogRowMapper = authenticationLogRowMapper;
    }

    @Override
    public void createAuthenticationLog(AuthenticationLog authenticationLog) {
        jdbcTemplate.update(
                "INSERT INTO authentication_log (user_account_id, principal, incident_timestamp, authentication_event_type, authenticated) VALUES (?, ?, ?, ?, ?)",
                authenticationLog.getUserAccountId(),
                authenticationLog.getPrincipal(),
                new Timestamp(authenticationLog.getIncidentTimestamp().toEpochMilli()),
                authenticationLog.getAuthenticationEventType(),
                authenticationLog.isAuthenticated());
    }

    @Override
    public List<AuthenticationLog> findAllByUserId(Long userId) {
        return jdbcTemplate.query(
                "SELECT user_account_id, principal, incident_timestamp, authentication_event_type, authenticated FROM authentication_log WHERE user_account_id=?",
                new Object[]{userId},
                authenticationLogRowMapper);
    }

    @Override
    public List<AuthenticationLog> findAllByPrincipal(String principal) {
        return jdbcTemplate.query(
                "SELECT user_account_id, principal, incident_timestamp, authentication_event_type, authenticated FROM authentication_log WHERE principal=?",
                new Object[]{principal},
                authenticationLogRowMapper);
    }

    @Override
    public List<AuthenticationLog> findByUserIdOrderedByIncidentTimestampDesc(Long userId, int limit) {
        return jdbcTemplate.query(
                "SELECT user_account_id, principal, incident_timestamp, authentication_event_type, authenticated FROM authentication_log WHERE user_account_id=? ORDER BY incident_timestamp DESC LIMIT ?",
                new Object[]{userId, limit},
                authenticationLogRowMapper);
    }
}
