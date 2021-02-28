package pl.kubaretip.chatservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.OffsetDateTime;


@Table(schema = "chat_service_database",
        name = "friend_request",
        uniqueConstraints = @UniqueConstraint(columnNames = {"sender_chat_profile_id", "recipient_chat_profile_id"}))
@Entity
@Getter
@Setter
@NoArgsConstructor
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "sent_time")
    private OffsetDateTime sentTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_chat_profile_id", nullable = false)
    private ChatProfile sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_chat_profile_id", nullable = false)
    private ChatProfile recipient;

    @Column(nullable = false, name = "is_accepted")
    private boolean isAccepted = false;

}
