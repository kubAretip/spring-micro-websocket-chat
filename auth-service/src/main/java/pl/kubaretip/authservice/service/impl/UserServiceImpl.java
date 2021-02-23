package pl.kubaretip.authservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kubaretip.authservice.constants.AuthorityConstants;
import pl.kubaretip.authservice.domain.Authority;
import pl.kubaretip.authservice.domain.User;
import pl.kubaretip.authservice.repository.AuthorityRepository;
import pl.kubaretip.authservice.repository.UserRepository;
import pl.kubaretip.authservice.service.UserService;
import pl.kubaretip.authutils.SecurityUtils;
import pl.kubaretip.exceptionutils.AlreadyExistsException;
import pl.kubaretip.exceptionutils.InvalidDataException;
import pl.kubaretip.exceptionutils.NotFoundException;

import java.util.HashSet;
import java.util.UUID;

@Slf4j
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
            throw new AlreadyExistsException("User with username " + username + " already exists.");
        }

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new AlreadyExistsException("User with email " + email + " already exists.");
        }

        var user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setActivationKey(RandomStringUtils.randomAlphanumeric(124));
        user.setFirstName(StringUtils.capitalize(firstName.toLowerCase()));
        user.setLastName(StringUtils.capitalize(lastName.toLowerCase()));
        user.setEmail(email);

        var authorities = new HashSet<Authority>();
        authorityRepository.findByNameIgnoreCase(AuthorityConstants.ROLE_USER.name())
                .ifPresent(authorities::add);
        user.setAuthorities(authorities);

        return userRepository.save(user);
    }


    @Override
    public void activateUser(String activationKey) {

        if (!StringUtils.isNotBlank(activationKey))
            throw new InvalidDataException("Invalid activation key");

        userRepository.findOneByActivationKey(activationKey)
                .ifPresent(user -> {
                    log.debug("Activation user with id {} with key {}", user.getId(), activationKey);
                    user.setEnabled(true);
                    user.setActivationKey(null);
                    userRepository.save(user);

                });
    }


    @Override
    public User findUserById(String userId) {
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new NotFoundException("Not found user with id " + userId));
    }


    @Override
    public User modifyUser(String userId, String firstName, String lastName) {

        var user = findUserById(userId);

        if (!user.getId().toString().equals(SecurityUtils.getCurrentUser())) {
            throw new InvalidDataException("Incorrect user id");
        }

        if (StringUtils.isNotEmpty(firstName)) {
            user.setFirstName(StringUtils.capitalize(firstName.toLowerCase()));
        }

        if (StringUtils.isNotEmpty(lastName)) {
            user.setFirstName(StringUtils.capitalize(lastName.toLowerCase()));
        }

        return userRepository.save(user);
    }


}
