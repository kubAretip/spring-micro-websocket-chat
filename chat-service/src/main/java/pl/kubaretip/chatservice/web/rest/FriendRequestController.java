package pl.kubaretip.chatservice.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pl.kubaretip.authutils.SecurityUtils;
import pl.kubaretip.chatservice.dto.FriendRequestDTO;
import pl.kubaretip.chatservice.dto.mapper.FriendRequestMapper;
import pl.kubaretip.chatservice.service.FriendRequestService;

import java.util.List;

@RestController
@RequestMapping("/friend-requests")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;
    private final FriendRequestMapper friendRequestMapper;

    public FriendRequestController(FriendRequestService friendRequestService,
                                   FriendRequestMapper friendRequestMapper) {
        this.friendRequestService = friendRequestService;
        this.friendRequestMapper = friendRequestMapper;
    }

    @PostMapping(params = {"invite_code"})
    public ResponseEntity<FriendRequestDTO> createNewFriendRequest(@RequestParam("invite_code") String inviteCode,
                                                                   UriComponentsBuilder uriComponentsBuilder) {
        var newFriendsRequest = friendRequestService.createNewFriendRequest(SecurityUtils.getCurrentUser(), inviteCode);
        var location = uriComponentsBuilder.path("/friends/{id}")
                .buildAndExpand(newFriendsRequest.getId()).toUri();
        return ResponseEntity.created(location).body(friendRequestMapper.mapToFriendRequestDTO(newFriendsRequest));
    }

    @PatchMapping(path = "/{id}", params = {"accept"})
    public ResponseEntity<Void> replyToFriendRequest(@PathVariable("id") long friendRequestId,
                                                     @RequestParam("accept") boolean accept) {
        friendRequestService.replyToFriendRequest(friendRequestId, SecurityUtils.getCurrentUser(), accept);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSenderFriendRequestById(@PathVariable("id") long friendRequestId) {
        friendRequestService.deleteFriendRequestBySender(SecurityUtils.getCurrentUser(), friendRequestId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/received")
    public ResponseEntity<List<FriendRequestDTO>> getNotAcceptedRecipientFriendRequests() {
        var friendRequests = friendRequestService.getAllNotAcceptedFriendRequestsByRecipientId(SecurityUtils.getCurrentUser());
        return ResponseEntity.ok()
                .body(friendRequestMapper.mapToFriendRequestDTOListWithoutRecipient(friendRequests));
    }

    @GetMapping("/sent")
    public ResponseEntity<List<FriendRequestDTO>> getNotAcceptedSenderFriendRequests() {
        var friendRequests = friendRequestService.getAllNotAcceptedFriendRequestsBySenderId(SecurityUtils.getCurrentUser());
        return ResponseEntity.ok()
                .body(friendRequestMapper.mapToFriendRequestDTOListWithoutSender(friendRequests));
    }

}
