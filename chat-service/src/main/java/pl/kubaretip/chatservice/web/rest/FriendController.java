package pl.kubaretip.chatservice.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kubaretip.authutils.SecurityUtils;
import pl.kubaretip.chatservice.domain.Friend;
import pl.kubaretip.chatservice.service.FriendService;


@RestController
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @PostMapping(params = {"invite_code"})
    public ResponseEntity<Friend> createNewFriend(@RequestParam("invite_code") String inviteCode) {
        var newFriendsRequest = friendService.createNewFriend(SecurityUtils.getCurrentUser(), inviteCode);
        return ResponseEntity.ok(newFriendsRequest);
    }

    @PatchMapping(path = "/{id}", params = {"accept"})
    public ResponseEntity<Friend> replyToFriendsRequest(@PathVariable("id") long friendId,
                                                        @RequestParam("accept") boolean accept) {
        friendService.replyToFriendsRequest(friendId, SecurityUtils.getCurrentUser(), accept);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSentFriendsRequestById(@PathVariable("id") long friendId) {
        friendService.deleteFriendsRequest(SecurityUtils.getCurrentUser(), friendId);
        return ResponseEntity.noContent().build();
    }


}
