package pl.kubaretip.chatservice.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kubaretip.authutils.SecurityUtils;
import pl.kubaretip.chatservice.dto.ChatProfileDTO;
import pl.kubaretip.chatservice.dto.mapper.ChatProfileMapper;
import pl.kubaretip.chatservice.service.ChatProfileService;

@RestController
@RequestMapping("/chat-profiles")
public class ChatProfileController {

    private final ChatProfileService chatProfileService;
    private final ChatProfileMapper chatProfileMapper;

    public ChatProfileController(ChatProfileService chatProfileService,
                                 ChatProfileMapper chatProfileMapper) {
        this.chatProfileService = chatProfileService;
        this.chatProfileMapper = chatProfileMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatProfileDTO> getChatProfileById(@PathVariable("id") String id) {
        return ResponseEntity.ok()
                .body(chatProfileMapper.chatProfileToChatProfileDTO(chatProfileService.getChatProfileById(id)));
    }

    @PatchMapping("/{id}/new-friends-request-code")
    public ResponseEntity<Void> generateNewFriendsRequestCode(@PathVariable("id") String userId) {
        chatProfileService.generateNewFriendsRequestCode(userId, SecurityUtils.getCurrentUserPreferredUsername());
        return ResponseEntity.noContent().build();
    }


}
