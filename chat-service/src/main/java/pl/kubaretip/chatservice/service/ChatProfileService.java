package pl.kubaretip.chatservice.service;

import pl.kubaretip.chatservice.domain.ChatProfile;

public interface ChatProfileService {
    ChatProfile createChatProfile(String userId, String username);

    ChatProfile generateNewFriendsRequestCode(String userId, String username);

    ChatProfile getChatProfileById(String userId);
}
