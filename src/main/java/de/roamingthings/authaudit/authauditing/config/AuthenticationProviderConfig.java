package de.roamingthings.authaudit.authauditing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import javax.sql.DataSource;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/11
 */
//@Configuration
public class AuthenticationProviderConfig {

    @Bean(name = "userDetailsService")
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcDaoImpl jdbcImpl = new JdbcDaoImpl();
        jdbcImpl.setDataSource(dataSource);
        jdbcImpl.setUsersByUsernameQuery("SELECT username, password_hash, enabled FROM user WHERE username=?");
        jdbcImpl.setAuthoritiesByUsernameQuery("SELECT u.username, r.role FROM user u LEFT JOIN user_role ur ON u.id = ur.user_id LEFT JOIN role r on ur.role_id = r.id where u.username=?");
        return jdbcImpl;
    }

}
