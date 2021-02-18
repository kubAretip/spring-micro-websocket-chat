package pl.kubaretip.userservice.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pl.kubaretip.userservice.cilent.AuthUserClient;
import pl.kubaretip.userservice.domain.User;
import pl.kubaretip.dtomodels.UserDTO;
import pl.kubaretip.userservice.exception.AlreadyExistsException;
import pl.kubaretip.userservice.repository.UserRepository;
import pl.kubaretip.userservice.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthUserClient authUserClient;

    public UserServiceImpl(UserRepository userRepository,
                           AuthUserClient authUserClient) {
        this.userRepository = userRepository;
        this.authUserClient = authUserClient;
    }


    @Override
    public User createUser(UserDTO userDTO) {

        userRepository.findOneByUsernameIgnoreCase(userDTO.getUsername())
                .ifPresent(user -> {
                    throw new AlreadyExistsException("User " + userDTO.getUsername() + " already exists");
                });

        userRepository.findOneByEmailIgnoreCase(userDTO.getEmail())
                .ifPresent(existingUser -> {
                    throw new AlreadyExistsException("User with address email " + userDTO.getEmail() + " already exists");
                });

        var user = new User();

        user.setUsername(userDTO.getUsername());
        user.setFirstName(StringUtils.capitalize(userDTO.getFirstName()));
        user.setLastName(StringUtils.capitalize(userDTO.getLastName()));
        user.setEmail(userDTO.getEmail());
        user.setFriendRequestCode(generateFriendRequestCode(user.getUsername().toLowerCase()));

        authUserClient.createAuthUser(userDTO.getUsername(), userDTO.getPassword());

        return userRepository.save(user);
    }

    private String generateFriendRequestCode(String login) {
        return login.toLowerCase() + "-" + RandomStringUtils.randomNumeric(10);
    }


}
