package pl.kubaretip.chatservice.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.kubaretip.chatservice.constants.FriendsRequestStatus;

import java.time.OffsetDateTime;

@Document(collection = "friends_request")
@Getter
@Setter
@NoArgsConstructor
public class FriendsRequest {

    @Id
    private String id;

    public OffsetDateTime time;

    @DBRef(lazy = true)
    private ChatProfile sender;

    @DBRef(lazy = true)
    private ChatProfile recipient;

    private FriendsRequestStatus status;

}
