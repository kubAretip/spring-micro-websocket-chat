package pl.kubaretip.chatmessagesservice.service;

import pl.kubaretip.chatmessagesservice.document.ChatMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChatMessageService {

    Flux<ChatMessage> findLastUsersMessagesFromTime(long firstUserFriendChatId, long secondUserFriendChatId,
                                                    String beforeTime, int numberOfMessagesToFetch);

    Flux<ChatMessage> getLastUserMessages(long friendChatId1, long friendChatId2, int numberOfMessagesToFetch);

    Mono<Void> setDeliveredStatusForAllRecipientMessagesInFriendChat(long friendChatId);

    Mono<ChatMessage> saveChatMessage(Long friendChat, String sender, String recipient, String content, String time);
}
