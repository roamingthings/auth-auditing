package de.roamingthings.authaudit.authauditing.service;

import de.roamingthings.authaudit.authauditing.domain.Role;
import de.roamingthings.authaudit.authauditing.domain.UserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/15
 */
public class UserAccountDetails extends User {
    private Long userId;

    public UserAccountDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, Long userId) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
    }

    public UserAccountDetails(Long userId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
    }

    public static UserAccountDetails of(UserAccount userAccount, boolean nonLocked) {
        final UserAccountDetails userAccountDetails = new UserAccountDetails(
                userAccount.getUsername(),
                userAccount.getPasswordHash(),
                true,
                false,
                false,
                nonLocked,
                grantedAuthorities(userAccount),
                userAccount.getId()
                );
        return userAccountDetails;
    }

    private static List<GrantedAuthority> grantedAuthorities(UserAccount userAccount) {
        return userAccount.getRoles().stream()
                .map(Role::getRole)
                .map(SimpleGrantedAuthority::new)
                .collect(toList());
    }

    public Long getUserId() {
        return userId;
    }
}
