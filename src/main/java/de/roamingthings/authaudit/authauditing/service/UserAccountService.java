package de.roamingthings.authaudit.authauditing.service;


import de.roamingthings.authaudit.authauditing.domain.Role;
import de.roamingthings.authaudit.authauditing.domain.UserAccount;

import java.util.Optional;
import java.util.Set;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/03
 */
public interface UserAccountService {
    UserAccount findByUsername(String username);

    UserAccount addEnabledUserWithRolesIfNotExists(String username, String password, Role... roles);

    UserAccount addEnabledUserWithRolesIfNotExists(String username, String password, Set<Role> roleSet);

    UserAccount addEnabledUserWithRolesIfNotExists(String username, String password, String... roles);

    Optional<Long> findUserIdByUsername(String username);
}
