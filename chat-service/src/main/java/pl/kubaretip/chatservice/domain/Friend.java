package pl.kubaretip.chatservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import pl.kubaretip.chatservice.constants.FriendStatus;

import javax.persistence.*;
import java.time.OffsetDateTime;


@Table(schema = "chat_service_database",
        uniqueConstraints = @UniqueConstraint(columnNames = {"sender_chat_profile_id", "recipient_chat_profile_id"}))
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Friend {

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "friends_request_status")
    private FriendStatus friendsRequestStatus;

}
