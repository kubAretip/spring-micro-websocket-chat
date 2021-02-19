package pl.kubaretip.mailservice.messaging.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.kubaretip.dtomodels.UserDTO;
import pl.kubaretip.mailservice.service.SendMailService;

@Component
public class RabbitUserActivationListener {

    private final SendMailService sendMailService;

    public RabbitUserActivationListener(SendMailService sendMailService) {
        this.sendMailService = sendMailService;
    }

    @RabbitListener(queues = "pl.kubaretip.mailservice.users.activation")
    public void receiveNewUser(UserDTO userDTO) {
        sendMailService.sendActivationEmail(userDTO);
    }


}
