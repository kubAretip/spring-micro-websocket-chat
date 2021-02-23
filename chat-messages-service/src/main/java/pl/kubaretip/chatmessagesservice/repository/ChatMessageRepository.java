package pl.kubaretip.chatmessagesservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pl.kubaretip.chatmessagesservice.document.ChatMessage;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.Date;


public interface ChatMessageRepository extends ReactiveCrudRepository<ChatMessage, String> {

    Flux<ChatMessage> findByTimeLessThanAndFriendChatIn(Date time, Collection<Long> ids, Pageable pageable);


    Flux<ChatMessage> findByFriendChatOrFriendChat(long firstUserChatId, long secondUserChatId, Pageable pageable);


}
