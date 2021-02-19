package pl.kubaretip.authservice.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.kubaretip.authservice.repository.UserRepository;
import pl.kubaretip.authutils.security.SecurityUserDetailsImpl;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Fetching given user {}", username);

        return userRepository.findOneWithAuthoritiesByUsernameIgnoreCase(username)
                .map(user -> new SecurityUserDetailsImpl(user.getId().toString(),
                        user.getUsername(), user.getPassword(), user.getEnabled(),
                        user.getAuthorities().stream()
                                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                                .collect(Collectors.toList()))
                ).orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    }
}
