package de.roamingthings.authaudit.authauditing.domain;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/12
 */
public enum AuthenticationEventType {
    LOGGED_IN_SUCCESSFUL, LOGGED_OUT, ACCOUNT_INVALID, ACCOUNT_DISABLED, ACCOUNT_LOCKED, ACCOUNT_EXPIRED, CREDENTIALS_NOT_FOUND, PASSWORD_CHANGED
}
