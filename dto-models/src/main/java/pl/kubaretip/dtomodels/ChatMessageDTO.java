package pl.kubaretip.dtomodels;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDTO implements Serializable {

    private String id;
    private Long friendChat;
    private String sender;
    private String recipient;
    private String content;
    private String time;
    private String status;

}
