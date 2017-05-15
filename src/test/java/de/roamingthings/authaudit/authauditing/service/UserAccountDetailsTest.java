package de.roamingthings.authaudit.authauditing.service;

import de.roamingthings.authaudit.authauditing.domain.Role;
import de.roamingthings.authaudit.authauditing.domain.UserAccount;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/15
 */
public class UserAccountDetailsTest {
    @Test
    public void should_create_instance_from_entity() throws Exception {
        UserAccount userAccount = new UserAccount(
                "username",
                "hash",
                true,
                Stream.of("ROLE_USER", "ROLE_ADMIN").map(Role::new).collect(toSet())
        );

        final UserDetails userDetails = UserAccountDetails.of(userAccount);

        assertThat(userDetails.getUsername(), is(userAccount.getUsername()));
        assertThat(userDetails.getAuthorities(), hasItems(
                hasProperty("authority", is("ROLE_USER")),
                hasProperty("authority", is("ROLE_ADMIN"))
        ));
    }
}