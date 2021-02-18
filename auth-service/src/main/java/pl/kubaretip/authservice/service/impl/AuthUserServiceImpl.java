package pl.kubaretip.authservice.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kubaretip.authservice.constants.AuthorityConstants;
import pl.kubaretip.authservice.domain.AuthUser;
import pl.kubaretip.authservice.domain.Authority;
import pl.kubaretip.authservice.exception.UserAlreadyExistsException;
import pl.kubaretip.authservice.repository.AuthUserRepository;
import pl.kubaretip.authservice.repository.AuthorityRepository;
import pl.kubaretip.authservice.service.AuthUserService;

import java.util.HashSet;

@Service
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthUserRepository authUserRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthUserServiceImpl(AuthUserRepository authUserRepository,
                               AuthorityRepository authorityRepository,
                               PasswordEncoder passwordEncoder) {
        this.authUserRepository = authUserRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthUser createAuthUser(String username, String password) {

        if (authUserRepository.existsByUsernameIgnoreCase(username)) {
            throw new UserAlreadyExistsException("Account with username " + username + " already exists.");
        }

        var authUser = new AuthUser();
        authUser.setUsername(username);
        authUser.setPassword(passwordEncoder.encode(password));
        authUser.setActivationKey(RandomStringUtils.randomAlphanumeric(124));
        var authorities = new HashSet<Authority>();
        authorityRepository.findByNameIgnoreCase(AuthorityConstants.ROLE_USER.name())
                .ifPresent(authorities::add);
        authUser.setAuthorities(authorities);

        return authUserRepository.save(authUser);
    }


}
