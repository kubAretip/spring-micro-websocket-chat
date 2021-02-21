package pl.kubaretip.chatservice.service;

import pl.kubaretip.chatservice.domain.Friend;

import java.util.List;

public interface FriendService {
    Friend createNewFriend(String senderUserId, String friendRequestCode);

    void replyToFriendsRequest(long friendId, String currentUserId, boolean accept);

    void deleteFriendsRequest(String currentUserId, long friendId);

    List<Friend> getAllRecipientFriendsWithSentStatus(String currentUser);
}
