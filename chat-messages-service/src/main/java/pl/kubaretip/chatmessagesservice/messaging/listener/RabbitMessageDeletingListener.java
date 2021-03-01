package pl.kubaretip.chatmessagesservice.messaging.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.kubaretip.chatmessagesservice.service.ChatMessageService;

import java.util.List;

@Component
public class RabbitMessageDeletingListener {

    private final ChatMessageService chatMessageService;

    public RabbitMessageDeletingListener(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @RabbitListener(queues = "#{messageDeletingQueue.name}")
    public void receiveNewChatMessage(List<Long> friendChatIds) {
        chatMessageService.removeMessagesByFriendChat(friendChatIds).subscribe();
    }

}
