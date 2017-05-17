package de.roamingthings.authaudit.authauditing.service;

import de.roamingthings.authaudit.authauditing.domain.AuthenticationLog;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.switchuser.AuthenticationSwitchUserEvent;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/16
 */
public class AuthenticationEventTypePolicy {
    private static List<String> successfulEventTypes;
    static {
        successfulEventTypes = Stream.of(AuthenticationSuccessEvent.class, AuthenticationSwitchUserEvent.class)
                .map(Class::getName)
                .collect(toList());
    }
    public static boolean isSuccessFullAuthenticationEvent(AuthenticationLog authenticationLog) {
        final String authenticationEventType = authenticationLog.getAuthenticationEventType();
        return successfulEventTypes.contains(authenticationEventType);
    }
}
