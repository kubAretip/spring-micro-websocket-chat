package pl.kubaretip.authservice.service.impl;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pl.kubaretip.authservice.service.UserActivationMessagingService;
import pl.kubaretip.dtomodels.UserDTO;

@Service
public class UserActivationMessagingServiceImpl implements UserActivationMessagingService {

    private RabbitTemplate rabbitTemplate;

    public UserActivationMessagingServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendActivationMail(UserDTO userDTO) {
        var converter = rabbitTemplate.getMessageConverter();
        var messageProperties = new MessageProperties();
        var message = converter.toMessage(userDTO, messageProperties);
        rabbitTemplate.send("pl.kubaretip.mailservice.users.activation", message);
    }

}

