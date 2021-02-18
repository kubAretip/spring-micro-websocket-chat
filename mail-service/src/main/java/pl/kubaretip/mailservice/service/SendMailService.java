package pl.kubaretip.mailservice.service;

import pl.kubaretip.dtomodels.UserDTO;

public interface SendMailService {

    void sendActivationEmail(UserDTO user, String activationProcessingUrl);
}
