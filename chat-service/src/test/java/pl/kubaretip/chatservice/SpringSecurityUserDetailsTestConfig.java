package pl.kubaretip.chatservice;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import pl.kubaretip.authutils.security.SecurityUserDetailsImpl;

import java.util.Collections;

@TestConfiguration
public class SpringSecurityUserDetailsTestConfig {

    public static final String MOCK_USER_ID = "25f5b4ec-cc35-40e9-ba77-a216fd70b85c";

    @Primary
    @Bean
    public UserDetailsService testUserDetailsService() {
        return username -> new SecurityUserDetailsImpl(MOCK_USER_ID,
                username, "fake_pass", true,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }


}
