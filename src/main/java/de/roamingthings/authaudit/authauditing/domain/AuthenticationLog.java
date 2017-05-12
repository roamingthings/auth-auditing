package de.roamingthings.authaudit.authauditing.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/12
 */
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class AuthenticationLog {
    private Long userId;

    private String principal;

    private Instant incidentTimestamp;

    private AuthenticationEventType authenticationEventType;

    public AuthenticationLog(Long userId, String principal, Instant incidentTimestamp, AuthenticationEventType authenticationEventType) {
        this.userId = userId;
        this.principal = principal;
        this.incidentTimestamp = incidentTimestamp;
        this.authenticationEventType = authenticationEventType;
    }

}
