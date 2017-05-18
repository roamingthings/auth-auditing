package de.roamingthings.authaudit.authauditing.service;

import de.roamingthings.authaudit.authauditing.domain.Role;
import de.roamingthings.authaudit.authauditing.domain.UserAccount;
import org.junit.Test;

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
        userAccount.setId(1L);

        final UserAccountDetails userDetails = UserAccountDetails.of(userAccount, true);

        assertThat(userDetails.getUserId(), is(userAccount.getId()));
        assertThat(userDetails.getUsername(), is(userAccount.getUsername()));
        assertThat(userDetails.getAuthorities(), hasItems(
                hasProperty("authority", is("ROLE_USER")),
                hasProperty("authority", is("ROLE_ADMIN"))
        ));
        assertThat(userDetails.isAccountNonExpired(), is(true));
        assertThat(userDetails.isAccountNonLocked(), is(true));
        assertThat(userDetails.isCredentialsNonExpired(), is(true));
        assertThat(userDetails.isEnabled(), is(true));
    }
}