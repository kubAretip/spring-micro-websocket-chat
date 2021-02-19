package pl.kubaretip.authservice.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kubaretip.authservice.constants.AuthorityConstants;
import pl.kubaretip.authservice.domain.Authority;
import pl.kubaretip.authservice.domain.User;
import pl.kubaretip.authservice.exception.UserAlreadyExistsException;
import pl.kubaretip.authservice.repository.AuthorityRepository;
import pl.kubaretip.authservice.repository.UserRepository;
import pl.kubaretip.authservice.service.UserService;

import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           AuthorityRepository authorityRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(String username, String password, String email, String firstName, String lastName) {

        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new UserAlreadyExistsException("User with username " + username + " already exists.");
        }

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new UserAlreadyExistsException("User with email " + email + " already exists.");
        }

        var user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setActivationKey(RandomStringUtils.randomAlphanumeric(124));
        user.setFirstName(StringUtils.capitalize(firstName));
        user.setLastName(StringUtils.capitalize(lastName));
        user.setEmail(email);

        var authorities = new HashSet<Authority>();
        authorityRepository.findByNameIgnoreCase(AuthorityConstants.ROLE_USER.name())
                .ifPresent(authorities::add);
        user.setAuthorities(authorities);

        return userRepository.save(user);
    }


}
