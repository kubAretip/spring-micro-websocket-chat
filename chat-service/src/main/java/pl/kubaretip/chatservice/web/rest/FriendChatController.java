package pl.kubaretip.chatservice.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kubaretip.authutils.SecurityUtils;
import pl.kubaretip.chatservice.dto.FriendChatDTO;
import pl.kubaretip.chatservice.dto.mapper.FriendChatMapper;
import pl.kubaretip.chatservice.service.FriendChatService;

import java.util.List;

@RestController
@RequestMapping("/friends-chats")
public class FriendChatController {

    private final FriendChatService friendChatService;
    private final FriendChatMapper friendChatMapper;

    public FriendChatController(FriendChatService friendChatService,
                                FriendChatMapper friendChatMapper) {
        this.friendChatService = friendChatService;
        this.friendChatMapper = friendChatMapper;
    }

    @GetMapping
    public ResponseEntity<List<FriendChatDTO>> getAllFriendsChats() {

        var allFriendsChatsBySender = friendChatService.getAllFriendsChatsBySender(SecurityUtils.getCurrentUser());

        return ResponseEntity.ok()
                .body(friendChatMapper.mapToFriendChatList(allFriendsChatsBySender));
    }


}
