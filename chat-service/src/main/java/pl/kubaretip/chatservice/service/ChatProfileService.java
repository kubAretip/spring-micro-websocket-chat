package pl.kubaretip.chatservice.service;

import pl.kubaretip.chatservice.domain.ChatProfile;
import reactor.core.publisher.Mono;

public interface ChatProfileService {
    ChatProfile createChatProfile(String userId, String username);
}
