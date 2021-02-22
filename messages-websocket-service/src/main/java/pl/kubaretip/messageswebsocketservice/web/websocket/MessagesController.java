package pl.kubaretip.messageswebsocketservice.web.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import pl.kubaretip.dtomodels.ChatMessageDTO;


@RestController
public class MessagesController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public MessagesController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageDTO chatMessage) {
        simpMessagingTemplate.convertAndSend("/topic/" + chatMessage.getRecipient() + ".messages", chatMessage);
    }

}
