package pl.kubaretip.chatmessagesservice.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.kubaretip.chatmessagesservice.document.ChatMessage;
import pl.kubaretip.chatmessagesservice.service.ChatMessageService;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat-messages")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @GetMapping(params = {"friend_chat_id1", "friend_chat_id2", "from", "size"})
    public Flux<ChatMessage> getLastUsersMessagesFromTimeWithSize(@RequestParam("friend_chat_id1") long friendChatId1,
                                                         @RequestParam("friend_chat_id2") long friendChatId2,
                                                         @RequestParam("from") String fromTime,
                                                         @RequestParam("size") int numberOfMessagesToFetch) {
        return chatMessageService.findLastUsersMessagesFromTime(friendChatId1, friendChatId2, fromTime, numberOfMessagesToFetch);
    }

    @GetMapping(params = {"friend_chat_id1", "friend_chat_id2", "size"})
    public Flux<ChatMessage> getLastUsersMessages(@RequestParam("friend_chat_id1") long friendChatId1,
                                                  @RequestParam("friend_chat_id2") long friendChatId2,
                                                  @RequestParam("size") int size) {

        return chatMessageService.getLastUserMessages(friendChatId1, friendChatId2, size);
    }


}
