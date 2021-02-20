package pl.kubaretip.chatservice.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "chat_profile")
@NoArgsConstructor
public class ChatProfile {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field(name = "user_id")
    private String userId;

    @Indexed(unique = true)
    @Field(name = "friends_request_code")
    private String friendsRequestCode;

}
