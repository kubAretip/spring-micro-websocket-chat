package pl.kubaretip.chatmessagesservice.messaging.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.kubaretip.chatmessagesservice.service.ChatMessageService;
import pl.kubaretip.dtomodels.ChatMessageDTO;

@Component
public class RabbitMessageStoringListener {

    private final ChatMessageService chatMessageService;

    public RabbitMessageStoringListener(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @RabbitListener(queues = "#{messageStoringQueue.name}")
    public void receiveNewChatMessage(ChatMessageDTO chatMessageDTO) {
        chatMessageService.saveChatMessage(chatMessageDTO).subscribe();
    }

}
