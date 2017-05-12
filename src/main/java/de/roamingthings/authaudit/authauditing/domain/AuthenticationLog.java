package de.roamingthings.authaudit.authauditing.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/12
 */
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "AUTHENTICATION_LOG")
public class AuthenticationLog {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "incident_timestamp", nullable = false)
    private Instant incidentTimestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "authentication_event_type")
    private AuthenticationEventType authenticationEventType;

    public AuthenticationLog(Long userId, Instant incidentTimestamp, AuthenticationEventType authenticationEventType) {
        this.userId = userId;
        this.incidentTimestamp = incidentTimestamp;
        this.authenticationEventType = authenticationEventType;
    }
}
