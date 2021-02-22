package pl.kubaretip.dtomodels;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDTO {

    private Long id;
    private String friendChat;
    private String sender;
    private String recipient;
    private String content;
    private String time;
    private String status;

}
