package de.roamingthings.authaudit.authauditing.repository;

import de.roamingthings.authaudit.authauditing.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/14
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
@ActiveProfiles("it")
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void should_find_user_id_of_created_user() throws Exception {
        User user = new User("user1", "abc", true, Collections.emptySet());
        final User createdUser = userRepository.save(user);

        final Optional<Long> foundUserId = userRepository.findUserIdByUsername(user.getUsername());
        assertThat(foundUserId.isPresent(), is(true));
        assertThat(foundUserId.get(), is(createdUser.getId()));
    }

}