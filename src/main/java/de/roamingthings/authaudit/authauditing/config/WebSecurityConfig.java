package de.roamingthings.authaudit.authauditing.config;

import de.roamingthings.authaudit.authauditing.repository.UserAccountRepository;
import de.roamingthings.authaudit.authauditing.service.AuthenticationLogService;
import de.roamingthings.authaudit.authauditing.service.UserAccountAuthenticationProvider;
import de.roamingthings.authaudit.authauditing.service.UserAccountPostAuthenticationChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/11
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private AuthenticationLogService authenticationLogService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/console/**").permitAll()
                .anyRequest().authenticated()
            .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
            .and()
                .logout()
                .permitAll()
            .and().csrf().ignoringAntMatchers("/console/**")
            .and().headers().frameOptions().sameOrigin();
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(userAccountAuthenticationProvider());
    }

    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider userAccountAuthenticationProvider() {
        UserAccountAuthenticationProvider userAccountAuthenticationProvider = new UserAccountAuthenticationProvider(userAccountRepository, passwordEncoder);
        userAccountAuthenticationProvider.setPostAuthenticationChecks(new UserAccountPostAuthenticationChecker(authenticationLogService));

        return userAccountAuthenticationProvider;
    }
}
