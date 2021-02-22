package pl.kubaretip.chatservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kubaretip.chatservice.domain.ChatProfile;
import pl.kubaretip.chatservice.domain.FriendChat;

public interface FriendChatRepository extends JpaRepository<FriendChat, Long> {


    @Query("SELECT " +
            "CASE WHEN COUNT (fc) > 0 THEN true ELSE false END " +
            "FROM FriendChat fc WHERE (fc.sender = :chatProfile1 AND fc.recipient = :chatProfile2) " +
            "OR (fc.sender = :chatProfile2 AND fc.recipient = :chatProfile1)")
    boolean existsFriendChatForUsers(ChatProfile chatProfile1, ChatProfile chatProfile2);


}
