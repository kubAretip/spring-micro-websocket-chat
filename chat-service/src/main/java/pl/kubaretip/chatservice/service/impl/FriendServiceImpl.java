package pl.kubaretip.chatservice.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.kubaretip.authutils.security.SecurityUserDetails;
import pl.kubaretip.chatservice.constants.FriendStatus;
import pl.kubaretip.chatservice.domain.Friend;
import pl.kubaretip.chatservice.exception.AlreadyExistsException;
import pl.kubaretip.chatservice.exception.InvalidDataException;
import pl.kubaretip.chatservice.exception.NotFoundException;
import pl.kubaretip.chatservice.repository.ChatProfileRepository;
import pl.kubaretip.chatservice.repository.FriendRepository;
import pl.kubaretip.chatservice.service.FriendService;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class FriendServiceImpl implements FriendService {

    private final ChatProfileRepository chatProfileRepository;
    private final FriendRepository friendRepository;

    public FriendServiceImpl(ChatProfileRepository chatProfileRepository, FriendRepository friendRepository) {
        this.chatProfileRepository = chatProfileRepository;
        this.friendRepository = friendRepository;
    }

    @Override
    public Friend createNewFriend(String senderUserId, String friendRequestCode) {

        var senderChatProfile = chatProfileRepository.findById(UUID.fromString(senderUserId))
                .orElseThrow(() -> new NotFoundException("Chat profile not found."));
        var recipientChatProfile = chatProfileRepository.findByFriendsRequestCode(friendRequestCode)
                .orElseThrow(() -> new NotFoundException("Friends request code not found"));

        if (senderChatProfile.getFriendsRequestCode().equals(friendRequestCode))
            throw new InvalidDataException("You can't send friends request to yourself.");

        if (friendRepository.isAlreadyFriends(senderChatProfile, recipientChatProfile)) {
            throw new AlreadyExistsException("Friends request already registered.");
        }

        var newFriend = new Friend();
        newFriend.setSender(senderChatProfile);
        newFriend.setRecipient(recipientChatProfile);
        newFriend.setFriendsRequestStatus(FriendStatus.SENT);
        newFriend.setSentTime(OffsetDateTime.now());
        return friendRepository.save(newFriend);
    }

    @Override
    public void replyToFriendsRequest(long friendId, String currentUserId, boolean accept) {

        var friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Friends request not found"));


        if (friend.getFriendsRequestStatus().equals(FriendStatus.ACCEPTED)) {
            throw new InvalidDataException("Friends request already accepted.");
        }

        if (!friend.getRecipient().getUserId().equals(UUID.fromString(currentUserId))) {
            throw new InvalidDataException("You are not recipient of this request");
        }


        if (accept)
            acceptFriendsRequest(friend);
        else
            deleteFriendRequest(friend);

    }

    private void deleteFriendRequest(Friend friend) {
        friendRepository.delete(friend);
    }

    private void acceptFriendsRequest(Friend friend) {
        friend.setFriendsRequestStatus(FriendStatus.ACCEPTED);
        friendRepository.save(friend);
    }


}
