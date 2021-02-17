package pl.kubaretip.authservice.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.kubaretip.authservice.repository.AuthUserRepository;

import javax.transaction.Transactional;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AuthUserRepository authUserRepository;

    public UserDetailsServiceImpl(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Fetching given user {}", username);

        return authUserRepository.findOneWithAuthoritiesByUsernameIgnoreCase(username)
                .map(SecurityUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    }
}
