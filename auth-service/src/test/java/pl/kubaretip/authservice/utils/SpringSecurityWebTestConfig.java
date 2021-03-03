package pl.kubaretip.authservice.utils;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import pl.kubaretip.authservice.domain.Authority;
import pl.kubaretip.authservice.domain.User;
import pl.kubaretip.authservice.repository.UserRepository;
import pl.kubaretip.authservice.security.UserDetailsServiceImpl;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


@TestConfiguration
public class SpringSecurityWebTestConfig {

    @Bean
    @Primary
    public UserDetailsService customUserDetailsService() {

        var userRepositoryMock = mock(UserRepository.class);

        var user = new User();
        user.setId(UUID.fromString("1062d618-64f6-401c-ab7c-ef050eb6f4b2"));
        user.setUsername("testUser");
        user.setFirstName("userFirstName");
        user.setLastName("userLastName");
        user.setEnabled(true);
        user.setPassword("password");
        var authority = new Authority();
        authority.setName("ROLE_USER");
        user.setAuthorities(Collections.singleton(authority));

        given(userRepositoryMock.findOneWithAuthoritiesByUsernameIgnoreCase(anyString())).willReturn(Optional.of(user));

        return new UserDetailsServiceImpl(userRepositoryMock);
    }

}
