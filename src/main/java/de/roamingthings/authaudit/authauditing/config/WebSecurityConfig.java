package de.roamingthings.authaudit.authauditing.config;

import de.roamingthings.authaudit.authauditing.service.UserAccountDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/11
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserAccountDetailsService userAccountDetailsService;

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
    public void configureGlobal(AuthenticationManagerBuilder auth, DataSource dataSource) throws Exception {
        auth
                .userDetailsService(userAccountDetailsService)
                .and()
                .jdbcAuthentication()
                .usersByUsernameQuery("SELECT username, password_hash, enabled FROM user_account WHERE username=?")
                .authoritiesByUsernameQuery("SELECT u.username, r.role FROM user_account u LEFT JOIN user_account_role ur ON u.id = ur.user_account_id LEFT JOIN role r on ur.role_id = r.id where u.username=?")
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder());
    }

/*
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
*/

    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
