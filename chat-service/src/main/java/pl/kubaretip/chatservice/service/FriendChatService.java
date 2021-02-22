package pl.kubaretip.chatservice.service;

import pl.kubaretip.chatservice.domain.ChatProfile;

public interface FriendChatService {
    void createFriendChat(ChatProfile firstUserChatProfile, ChatProfile secondUserChatProfile);
}
