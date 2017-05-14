package de.roamingthings.authaudit.authauditing.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.stereotype.Component;

import java.time.Clock;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/13
 */
@Component
public class AuthenticationEventListener implements ApplicationListener<AbstractAuthenticationEvent> {

    private static Logger logger = LogManager.getLogger(AuthenticationEventListener.class);

    private final AuthenticationLogService authenticationLogService;

    @Autowired
    public AuthenticationEventListener(Clock systemClock, AuthenticationLogService authenticationLogService) {
        this.authenticationLogService = authenticationLogService;
    }

    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent abstractAuthenticationEvent) {
        final Object authenticationEventSource = abstractAuthenticationEvent.getSource();

        String username = "<unknown>";
        boolean authenticated = false;

        if (authenticationEventSource instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authenticationEventSource;
            username = authenticationToken.getName();
            authenticated = authenticationToken.isAuthenticated();
        }

        authenticationLogService.createAuthenticationLogEntryForUserOfType(
                null,
                username,
                abstractAuthenticationEvent,
                authenticated);
    }
}