package de.roamingthings.authaudit.authauditing.repository;

import de.roamingthings.authaudit.authauditing.domain.AuthenticationEventType;
import de.roamingthings.authaudit.authauditing.domain.AuthenticationLog;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/13
 */
@Component
public class AuthenticationLogRowMapper implements RowMapper<AuthenticationLog> {
    @Override
    public AuthenticationLog mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        AuthenticationLog authenticationLog = new AuthenticationLog(
                resultSet.getLong(1),
                resultSet.getString(2),
                resultSet.getTimestamp(3).toInstant(),
                AuthenticationEventType.valueOf(resultSet.getString(4))
        );

        return authenticationLog;
    }
}
