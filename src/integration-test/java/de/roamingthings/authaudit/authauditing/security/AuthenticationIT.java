package de.roamingthings.authaudit.authauditing.security;

import de.roamingthings.authaudit.authauditing.AuthAuditingApplication;
import de.roamingthings.authaudit.authauditing.domain.UserAccount;
import de.roamingthings.authaudit.authauditing.service.AuthenticationLogService;
import de.roamingthings.authaudit.authauditing.service.UserAccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;
import java.util.UUID;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthAuditingApplication.class)
public class AuthenticationIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private AuthenticationLogService authenticationLogService;

    private MockMvc mockMvc;

    private UserAccount userAccount;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    public void should_redirect_to_login() throws Exception {
        createUserWithPassword("secret");

        assertRedirectToLogin();
    }

    @Test
    public void should_permit_login_page() throws Exception {
        createUserWithPassword("secret");

        mockMvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void should_login_successfully() throws Exception {
        createUserWithPassword("secret");

        assertSuccessfulLogin();
    }

    @Test
    public void should_login_unsuccessful_and_increase_series_size() throws Exception {
        createUserWithPassword("secret");

        assertUnsuccessfulLogin();

        final int unsuccessfulAuthenticationCount = authenticationLogService.unsuccessfulAuthenticationSeriesSize(userAccount.getId(), 5);
        assertThat(unsuccessfulAuthenticationCount, is(1));
    }

    private void assertUnsuccessfulLogin() throws Exception {
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", userAccount.getUsername())
                .param("password", "wrong")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?error"));
    }

    private void assertSuccessfulLogin() throws Exception {
        final HttpSession session = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", userAccount.getUsername())
                .param("password", "secret")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"))
                .andReturn()
                .getRequest()
                .getSession();

                assertNotNull(session);

        mockMvc.perform(get("/").session((MockHttpSession) session))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private void assertRedirectToLogin() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(header().string("Location", containsString("/login")));
    }

    private void createUserWithPassword(String password) throws Exception {
        userAccount = userAccountService.addEnabledUserWithRolesIfNotExists("itestuser" + UUID.randomUUID().toString(), password, "ROLE_USER", "ROLE_ADMIN");
    }
}
