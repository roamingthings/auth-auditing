package de.roamingthings.authaudit.authauditing.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;

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

    private String authenticationEventType;

    public AuthenticationLog(Long userId, String principal, Instant incidentTimestamp, String authenticationEventType) {
        this.userId = userId;
        this.principal = principal;
        this.incidentTimestamp = incidentTimestamp;
        this.authenticationEventType = authenticationEventType;
    }

    public static AuthenticationLog of(
            Long userId, String principal, Instant incidentTimestamp, Class<? extends AbstractAuthenticationEvent> eventClass) {
        return new AuthenticationLog(
                userId,
                principal,
                incidentTimestamp,
                eventClass.getName()
        );
    }

}
