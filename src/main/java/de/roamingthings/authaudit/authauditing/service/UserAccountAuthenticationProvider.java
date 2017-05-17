package de.roamingthings.authaudit.authauditing.service;

import de.roamingthings.authaudit.authauditing.domain.UserAccount;
import de.roamingthings.authaudit.authauditing.repository.UserAccountRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/16
 */
public class UserAccountAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private final UserAccountRepository userAccountRepository;
    private final AuthenticationLogService authenticationLogService;
    private final PasswordEncoder passwordEncoder;

    public UserAccountAuthenticationProvider(UserAccountRepository userAccountRepository, AuthenticationLogService authenticationLogService, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.authenticationLogService = authenticationLogService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        final String credentials = (String) authentication.getCredentials();
        if (!passwordEncoder.matches(credentials, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid credentials provided");
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        final UserAccount userAccount = userAccountRepository.findByUsername(username);
        if (userAccount == null) {
            throw new UsernameNotFoundException("No user account matches the combination of username and credentials");
        }

        return UserAccountDetails.of(
                userAccount,
                (authenticationLogService.unsuccessfulAuthenticationSeriesSize(userAccount.getId(), 5) < 5)
        );
    }
}
