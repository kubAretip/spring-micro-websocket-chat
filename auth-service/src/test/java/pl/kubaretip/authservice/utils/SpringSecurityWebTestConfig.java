package pl.kubaretip.authservice.utils;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import pl.kubaretip.authutils.security.SecurityUserDetailsImpl;

import java.util.Collections;
import java.util.UUID;


@TestConfiguration
public class SpringSecurityWebTestConfig {

    @Bean
    @Primary
    public UserDetailsService customUserDetailsService() {
        var user = new SecurityUserDetailsImpl(UUID.randomUUID().toString(),
                "testUser", "password", true,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        return new InMemoryUserDetailsManager(user);
    }

}
