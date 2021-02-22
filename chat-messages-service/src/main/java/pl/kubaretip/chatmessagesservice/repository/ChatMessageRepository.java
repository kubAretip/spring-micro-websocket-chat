package pl.kubaretip.chatmessagesservice.repository;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pl.kubaretip.chatmessagesservice.document.ChatMessage;

public interface ChatMessageRepository extends ReactiveCrudRepository<ChatMessage, String> {
}
