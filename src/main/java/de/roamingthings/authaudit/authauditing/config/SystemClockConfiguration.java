package de.roamingthings.authaudit.authauditing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/12
 */
@Configuration
public class SystemClockConfiguration {
    @Bean
    public Clock systemClock() {
        return Clock.systemDefaultZone();
    }
}
