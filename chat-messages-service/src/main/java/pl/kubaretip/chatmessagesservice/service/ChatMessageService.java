package pl.kubaretip.chatmessagesservice.service;

import pl.kubaretip.chatmessagesservice.document.ChatMessage;
import pl.kubaretip.dtomodels.ChatMessageDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChatMessageService {
    Mono<ChatMessage> saveChatMessage(ChatMessageDTO chatMessageDTO);

    Flux<ChatMessage> findLastUsersMessagesFromTime(long firstUserFriendChatId, long secondUserFriendChatId,
                                                         String beforeTime, int numberOfMessagesToFetch);

    Flux<ChatMessage> getLastUserMessages(long friendChatId1, long friendChatId2, int numberOfMessagesToFetch);
}
