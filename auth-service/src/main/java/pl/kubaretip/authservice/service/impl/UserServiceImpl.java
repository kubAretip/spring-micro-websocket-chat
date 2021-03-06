package pl.kubaretip.authservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kubaretip.authservice.constants.ApplicationConstants;
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

import static org.apache.commons.lang3.StringUtils.*;

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

        if (isEmpty(username) || isBlank(username) || !StringUtils.isAlphanumeric(username)) {
            throw new InvalidDataException("Invalid username");
        }
        if (isEmpty(password) || isBlank(password)) {
            throw new InvalidDataException("Invalid password");
        }
        if (password.length() < ApplicationConstants.USER_PASSWORD_MIN_LENGTH
                || password.length() > ApplicationConstants.USER_PASSWORD_MAX_LENGTH) {
            throw new InvalidDataException("Invalid password length");
        }
        if (!new EmailValidator().isValid(email, null)) {
            throw new InvalidDataException("Invalid email");
        }
        if (isEmpty(firstName) || isBlank(firstName)) {
            throw new InvalidDataException("Invalid first name");
        }
        if (isEmpty(lastName) || isBlank(lastName)) {
            throw new InvalidDataException("Invalid last name");
        }
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

        if (isBlank(activationKey))
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
        throwExceptionIfNotCurrentUser(user);

        if (isNotEmpty(firstName)) {
            user.setFirstName(StringUtils.capitalize(firstName.toLowerCase().replaceAll(" ", "")));
        }

        if (isNotEmpty(lastName)) {
            user.setLastName(StringUtils.capitalize(lastName.toLowerCase().replaceAll(" ", "")));
        }

        return userRepository.save(user);
    }


    @Override
    public void changeUserPassword(String userId, String currentPassword, String newPassword) {
        var user = findUserById(userId);
        throwExceptionIfNotCurrentUser(user);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidDataException("Incorrect current password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private void throwExceptionIfNotCurrentUser(User user) {
        if (!user.getId().toString().equals(SecurityUtils.getCurrentUser())) {
            throw new InvalidDataException("Incorrect user id");
        }
    }
}
