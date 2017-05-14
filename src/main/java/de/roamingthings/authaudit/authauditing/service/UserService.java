package de.roamingthings.authaudit.authauditing.service;


import de.roamingthings.authaudit.authauditing.domain.Role;
import de.roamingthings.authaudit.authauditing.domain.User;

import java.util.Optional;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/03
 */
public interface UserService {
    User findByUsername(String username);

    void addEnabledUserWithRolesIfNotExists(String username, String password, Role... roles);

    Optional<Long> findUserIdByUsername(String username);
}
