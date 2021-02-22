package pl.kubaretip.chatservice.service;

import pl.kubaretip.chatservice.domain.ChatProfile;
import pl.kubaretip.chatservice.domain.FriendChat;

import java.util.List;

public interface FriendChatService {
    void createFriendChat(ChatProfile firstUserChatProfile, ChatProfile secondUserChatProfile);

    List<FriendChat> getAllFriendsChatsBySender(String currentUser);
}
