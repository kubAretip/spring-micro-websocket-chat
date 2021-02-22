package pl.kubaretip.messageswebsocketservice.web.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import pl.kubaretip.dtomodels.ChatMessageDTO;
import pl.kubaretip.messageswebsocketservice.messaging.sender.StoringMessagesSender;


@RestController
public class MessagesController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final StoringMessagesSender storingMessagesSender;

    public MessagesController(SimpMessagingTemplate simpMessagingTemplate,
                              StoringMessagesSender storingMessagesSender) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.storingMessagesSender = storingMessagesSender;
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageDTO chatMessage) {
        simpMessagingTemplate.convertAndSend("/topic/" + chatMessage.getRecipient() + ".messages", chatMessage);
        storingMessagesSender.send(chatMessage);
    }

}
