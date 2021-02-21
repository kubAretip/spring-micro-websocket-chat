package pl.kubaretip.chatservice.service;

import pl.kubaretip.chatservice.domain.Friend;

public interface FriendService {
    Friend createNewFriend(String senderUserId, String friendRequestCode);

    void replyToFriendsRequest(long friendId, String currentUserId, boolean accept);
}
