package pl.kubaretip.chatservice.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pl.kubaretip.authutils.SecurityUtils;
import pl.kubaretip.chatservice.domain.Friend;
import pl.kubaretip.chatservice.dto.FriendDTO;
import pl.kubaretip.chatservice.dto.mapper.FriendMapper;
import pl.kubaretip.chatservice.service.FriendService;

import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService;
    private final FriendMapper friendMapper;

    public FriendController(FriendService friendService,
                            FriendMapper friendMapper) {
        this.friendService = friendService;
        this.friendMapper = friendMapper;
    }

    @PostMapping(params = {"invite_code"})
    public ResponseEntity<FriendDTO> createNewFriend(@RequestParam("invite_code") String inviteCode,
                                                     UriComponentsBuilder uriComponentsBuilder) {
        var newFriendsRequest = friendService.createNewFriend(SecurityUtils.getCurrentUser(), inviteCode);
        var location = uriComponentsBuilder.path("/friends/{id}")
                .buildAndExpand(newFriendsRequest.getId()).toUri();
        return ResponseEntity.created(location).body(friendMapper.mapToFriendDTO(newFriendsRequest));
    }

    @PatchMapping(path = "/{id}", params = {"accept"})
    public ResponseEntity<Void> replyToFriendsRequest(@PathVariable("id") long friendId,
                                                      @RequestParam("accept") boolean accept) {
        friendService.replyToFriendsRequest(friendId, SecurityUtils.getCurrentUser(), accept);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFriendsRequestByIdWithSentStatus(@PathVariable("id") long friendId) {
        friendService.deleteFriendsRequest(SecurityUtils.getCurrentUser(), friendId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public ResponseEntity<List<FriendDTO>> getReceivedFriendWithSentStatus() {
        var allRecipientFriendsWithSentStatus = friendService.getAllRecipientFriendsWithSentStatus(SecurityUtils.getCurrentUser());
        return ResponseEntity.ok()
                .body(friendMapper.mapToFriendDTOListWithoutRecipient(allRecipientFriendsWithSentStatus));
    }

}
