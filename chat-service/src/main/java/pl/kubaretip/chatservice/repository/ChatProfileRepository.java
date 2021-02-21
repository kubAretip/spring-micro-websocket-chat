package pl.kubaretip.chatservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kubaretip.chatservice.domain.ChatProfile;

import java.util.Optional;
import java.util.UUID;

public interface ChatProfileRepository extends JpaRepository<ChatProfile, UUID> {

    Optional<ChatProfile> findByFriendsRequestCode(String friendsRequestCode);

}
