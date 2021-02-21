package pl.kubaretip.chatservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;


@Getter
@Setter
@Table(name = "chat_profile", schema = "chat_service_database")
@Entity
@NoArgsConstructor
public class ChatProfile {

    @Id
    @Column(nullable = false, name = "user_id", unique = true, length = 36, columnDefinition = "BINARY(16)")
    private UUID userId;

    @Column(name = "friends_request_code", nullable = false, length = 64, unique = true)
    private String friendsRequestCode;

}
