package pl.kubaretip.userservice.service;

import pl.kubaretip.userservice.domain.User;
import pl.kubaretip.userservice.dto.UserDTO;

public interface UserService {
    User createUser(UserDTO userDTO);
}
