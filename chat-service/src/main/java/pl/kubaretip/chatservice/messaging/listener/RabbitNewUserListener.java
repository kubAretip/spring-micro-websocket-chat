package pl.kubaretip.chatservice.messaging.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.kubaretip.chatservice.service.ChatProfileService;
import pl.kubaretip.dtomodels.UserDTO;

@Slf4j
@Component
public class RabbitNewUserListener {

    private final ChatProfileService chatProfileService;

    public RabbitNewUserListener(ChatProfileService chatProfileService) {
        this.chatProfileService = chatProfileService;
    }


    @RabbitListener(queues = "#{newUsersQueue.name}")
    public void receiveNewUser(UserDTO userDTO) {
        log.debug("New user {}", userDTO.getUsername());
        chatProfileService.createChatProfile(userDTO.getId(), userDTO.getUsername()).subscribe();
    }


}
