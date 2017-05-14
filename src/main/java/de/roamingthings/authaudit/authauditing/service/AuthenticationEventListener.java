package de.roamingthings.authaudit.authauditing.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/13
 */
@Component
public class AuthenticationEventListener implements ApplicationListener<AbstractAuthenticationEvent> {

    private static Logger logger = LogManager.getLogger(AuthenticationEventListener.class);

    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent abstractAuthenticationEvent) {

        if (abstractAuthenticationEvent instanceof AbstractAuthenticationFailureEvent) {

            logger.warn("Detected an invalid login");

        } else if (abstractAuthenticationEvent instanceof AuthenticationSuccessEvent) {

            logger.info("A user logged in successfully");
        }
    }
}