package pl.kubaretip.chatservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kubaretip.chatservice.constants.FriendStatus;
import pl.kubaretip.chatservice.domain.ChatProfile;
import pl.kubaretip.chatservice.domain.Friend;

import java.util.List;
import java.util.UUID;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("SELECT " +
            "CASE WHEN COUNT(f) > 0 THEN true ELSE false END " +
            "FROM Friend f WHERE (f.sender = :user1 AND f.recipient = :user2) OR " +
            "(f.sender = :user2 AND f.recipient = :user1)")
    boolean isAlreadyFriends(ChatProfile user1, ChatProfile user2);

    @Query("SELECT f FROM Friend f WHERE f.recipient.userId = :recipientId AND f.friendsRequestStatus = :friendStatus")
    List<Friend> findByRecipientAndFriendsRequestStatus(UUID recipientId, FriendStatus friendStatus);

}
