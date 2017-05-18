package de.roamingthings.authaudit.authauditing.service;

import de.roamingthings.authaudit.authauditing.domain.Role;
import de.roamingthings.authaudit.authauditing.domain.UserAccount;
import de.roamingthings.authaudit.authauditing.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/03
 */
@Service
public class UserAccountServiceImpl implements UserAccountService {
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public UserAccountServiceImpl(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public UserAccount findByUsername(String username) {
        return userAccountRepository.findByUsername(username);
    }

    @Override
    public UserAccount addEnabledUserWithRolesIfNotExists(String username, String password, Role... roles) {
        final Set<Role> roleSet = Stream.of(roles).collect(toSet());
        return addEnabledUserWithRolesIfNotExists(username, password, roleSet);
    }

    @Override
    public UserAccount addEnabledUserWithRolesIfNotExists(String username, String password, Set<Role> roleSet) {
        UserAccount userAccount;

        if (!userAccountRepository.existsByUsername(username)) {
            final String passwordHash = passwordEncoder.encode(password);

            userAccount = userAccountRepository.save(new UserAccount(username, passwordHash, true, roleSet));
        } else {
            userAccount = userAccountRepository.findByUsername(username);
        }

        return userAccount;
    }

    @Override
    public UserAccount addEnabledUserWithRolesIfNotExists(String username, String password, String... roleNames) {
        final Set<Role> roles = Stream.of(roleNames)
                .map(roleService::findByRole)
                .collect(toSet());

        return addEnabledUserWithRolesIfNotExists(username, password, roles);
    }

    @Override
    public Optional<Long> findUserIdByUsername(String username) {
        return userAccountRepository.findUserIdByUsername(username);
    }
}
