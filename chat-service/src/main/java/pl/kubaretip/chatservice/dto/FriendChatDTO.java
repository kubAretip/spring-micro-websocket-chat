package pl.kubaretip.chatservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class FriendChatDTO {

    private Long id;
    private Long chatWith;
    private ChatProfileDTO recipient;

}
