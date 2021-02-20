package pl.kubaretip.chatservice.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pl.kubaretip.chatservice.document.ChatProfile;

public interface ChatProfileRepository extends ReactiveCrudRepository<ChatProfile, String> {
}
