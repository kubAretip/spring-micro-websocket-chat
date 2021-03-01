package pl.kubaretip.chatservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.kubaretip.chatservice.domain.ChatProfile;
import pl.kubaretip.chatservice.domain.FriendRequest;

import java.util.List;
import java.util.UUID;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    @Query("SELECT " +
            "CASE WHEN COUNT(fr) > 0 THEN true ELSE false END " +
            "FROM FriendRequest fr WHERE (fr.sender = :user1 AND fr.recipient = :user2) " +
            "OR (fr.sender = :user2 AND fr.recipient = :user1)")
    boolean isFriendRequestAlreadyExists(ChatProfile user1, ChatProfile user2);

    @Query("SELECT fr FROM FriendRequest fr WHERE fr.recipient.userId = :recipientId AND fr.isAccepted = false")
    List<FriendRequest> findAllByRecipientIdAndNotAccepted(UUID recipientId);

    @Query("SELECT fr FROM FriendRequest fr WHERE fr.sender.userId = :senderId AND fr.isAccepted = false")
    List<FriendRequest> findAllBySenderIdAndNotAccepted(UUID senderId);

    @Modifying
    @Query("DELETE FROM FriendRequest WHERE (sender = :sender AND recipient = :recipient) " +
            "OR (sender = :recipient AND recipient = :sender)")
    void deleteFriendRequestByChatProfiles(ChatProfile sender, ChatProfile recipient);

}
