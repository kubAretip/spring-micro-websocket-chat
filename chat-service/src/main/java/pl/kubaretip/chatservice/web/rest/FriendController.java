package pl.kubaretip.chatservice.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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


}
