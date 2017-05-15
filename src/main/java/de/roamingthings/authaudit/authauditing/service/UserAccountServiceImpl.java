package de.roamingthings.authaudit.authauditing.service;

import de.roamingthings.authaudit.authauditing.domain.Role;
import de.roamingthings.authaudit.authauditing.domain.UserAccount;
import de.roamingthings.authaudit.authauditing.repository.UserAccountRepository;
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

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserAccount findByUsername(String username) {
        return userAccountRepository.findByUsername(username);
    }

    @Override
    public void addEnabledUserWithRolesIfNotExists(String username, String password, Role... roles) {
        if (!userAccountRepository.existsByUsername(username)) {
            final String passwordHash = passwordEncoder.encode(password);

            final Set<Role> roleSet = Stream.of(roles).collect(toSet());
            UserAccount userAccount = new UserAccount(username, passwordHash, true, roleSet);
            userAccountRepository.save(userAccount);
        }
    }

    @Override
    public Optional<Long> findUserIdByUsername(String username) {
        return userAccountRepository.findUserIdByUsername(username);
    }
}
