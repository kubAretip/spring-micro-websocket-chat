package pl.kubaretip.chatservice.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kubaretip.authutils.SecurityUtils;
import pl.kubaretip.chatservice.service.ChatProfileService;

@RestController
@RequestMapping("/chat-profiles")
public class ChatProfileController {

    private final ChatProfileService chatProfileService;

    public ChatProfileController(ChatProfileService chatProfileService) {
        this.chatProfileService = chatProfileService;
    }

    @PatchMapping(value = "/{id}/new-friends-request-code")
    public ResponseEntity<Void> generateNewFriendsRequestCode(@PathVariable("id") String userId) {
        chatProfileService.generateNewFriendsRequestCode(userId, SecurityUtils.getCurrentUserPreferredUsername());
        return ResponseEntity.noContent().build();
    }


}
