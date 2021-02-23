package pl.kubaretip.chatservice.service;

import pl.kubaretip.chatservice.domain.ChatProfile;

public interface ChatProfileService {
    ChatProfile createChatProfile(String userId, String username);

    void generateNewFriendsRequestCode(String userId, String username);
}
