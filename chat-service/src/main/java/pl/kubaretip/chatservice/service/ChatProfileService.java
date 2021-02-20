package pl.kubaretip.chatservice.service;

import pl.kubaretip.chatservice.document.ChatProfile;
import reactor.core.publisher.Mono;

public interface ChatProfileService {
    Mono<ChatProfile> createChatProfile(String userId, String username);
}
