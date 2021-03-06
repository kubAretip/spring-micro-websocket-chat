package pl.kubaretip.authservice.service;

import pl.kubaretip.authservice.domain.User;

public interface UserService {
    User createUser(String username, String password, String email, String firstName, String lastName);

    void activateUser(String activationKey);

    User findUserById(String userId);

    User modifyUser(String userId, String firstName, String lastName);

    void changeUserPassword(String userId, String currentPassword, String newPassword);
}
