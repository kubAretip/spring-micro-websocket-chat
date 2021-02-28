package pl.kubaretip.chatservice.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class FriendRequestDTO {

    private Long id;
    private String sentTime;
    private ChatProfileDTO sender;
    private ChatProfileDTO recipient;
}
