package pl.kubaretip.authservice.service;

import pl.kubaretip.dtomodels.UserDTO;

public interface UserActivationMessagingService {
    void sendActivationMail(UserDTO userDTO);
}
