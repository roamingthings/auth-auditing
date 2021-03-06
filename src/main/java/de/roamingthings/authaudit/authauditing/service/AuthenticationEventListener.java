package de.roamingthings.authaudit.authauditing.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/13
 */
@Component
public class AuthenticationEventListener implements ApplicationListener<AbstractAuthenticationEvent> {

    private static Logger logger = LogManager.getLogger(AuthenticationEventListener.class);

    private final AuthenticationLogService authenticationLogService;
    private final UserAccountService userAccountService;

    @Autowired
    public AuthenticationEventListener(AuthenticationLogService authenticationLogService, UserAccountService userAccountService) {
        this.authenticationLogService = authenticationLogService;
        this.userAccountService = userAccountService;
    }

    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent abstractAuthenticationEvent) {
        final Object authenticationEventSource = abstractAuthenticationEvent.getSource();

        String username = "<unknown>";
        boolean authenticated = false;

        Long userId = null;
        if (authenticationEventSource instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authenticationEventSource;
            username = authenticationToken.getName();
            authenticated = authenticationToken.isAuthenticated();

            userId = userAccountService.findUserIdByUsername(username).orElse(null);
        }

        authenticationLogService.createAuthenticationLogEntryForUserOfType(
                userId,
                username,
                abstractAuthenticationEvent,
                authenticated);
    }
}