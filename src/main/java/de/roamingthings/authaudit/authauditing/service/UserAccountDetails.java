package de.roamingthings.authaudit.authauditing.service;

import de.roamingthings.authaudit.authauditing.domain.Role;
import de.roamingthings.authaudit.authauditing.domain.UserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/15
 */
public class UserAccountDetails extends User {
    public UserAccountDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public static UserDetails of(UserAccount userAccount) {
        return new UserAccountDetails(
                userAccount.getUsername(),
                userAccount.getPasswordHash(),
                grantedAuthorities(userAccount));
    }

    private static List<GrantedAuthority> grantedAuthorities(UserAccount userAccount) {
        return userAccount.getRoles().stream()
                .map(Role::getRole)
                .map(SimpleGrantedAuthority::new)
                .collect(toList());
    }
}
