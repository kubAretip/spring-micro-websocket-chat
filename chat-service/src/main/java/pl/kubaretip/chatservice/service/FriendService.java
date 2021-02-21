package pl.kubaretip.chatservice.service;

import pl.kubaretip.chatservice.domain.Friend;

import java.util.List;

public interface FriendService {
    Friend createNewFriend(String senderUserId, String friendRequestCode);

    void replyToFriend(long friendId, String currentUserId, boolean accept);

    void deleteFriendBySenderWithSentStatus(String currentUserId, long friendId);

    List<Friend> getAllRecipientFriendsWithSentStatus(String currentUser);

    List<Friend> getAllSenderFriendsByStatus(String senderUserId, String status);

}
