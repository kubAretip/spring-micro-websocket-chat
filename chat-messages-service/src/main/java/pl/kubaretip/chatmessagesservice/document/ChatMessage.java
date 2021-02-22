package pl.kubaretip.chatmessagesservice.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import pl.kubaretip.chatmessagesservice.constant.MessageStatus;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "chat_messages_service_database")
public class ChatMessage {

    @Id
    private String id;

    @Field(name = "friend_chat")
    private Long friendChat;
    private String sender;
    private String recipient;
    private String content;
    private Date time;
    private MessageStatus status;

}
