package pl.kubaretip.chatservice.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Table(schema = "chat_service_database", name = "friend_chat",
        uniqueConstraints = @UniqueConstraint(columnNames = {"chat_with_id", "sender_id", "recipient_id"}))
@Entity
public class FriendChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "chat_with_id")
    private FriendChat chatWith;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private ChatProfile sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private ChatProfile recipient;


}
