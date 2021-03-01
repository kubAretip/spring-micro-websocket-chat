package pl.kubaretip.chatservice.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kubaretip.chatservice.domain.ChatProfile;
import pl.kubaretip.chatservice.domain.FriendRequest;
import pl.kubaretip.chatservice.repository.ChatProfileRepository;
import pl.kubaretip.chatservice.repository.FriendRequestRepository;
import pl.kubaretip.chatservice.service.FriendChatService;
import pl.kubaretip.exceptionutils.AlreadyExistsException;
import pl.kubaretip.exceptionutils.InvalidDataException;
import pl.kubaretip.exceptionutils.NotFoundException;

import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FriendRequestServiceImplTest {

    @Mock
    private ChatProfileRepository chatProfileRepository;

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @Mock
    private FriendChatService friendChatService;

    @InjectMocks
    private FriendRequestServiceImpl friendRequestService;


    @Test
    public void shouldReturnNewFriendRequest() {

        // given
        var sender = new ChatProfile();
        sender.setFriendsRequestCode("");
        var recipient = new ChatProfile();
        var friendRequestCode = "123";
        recipient.setFriendsRequestCode(friendRequestCode);
        given(chatProfileRepository.findById(any(UUID.class))).willReturn(Optional.of(sender));
        given(chatProfileRepository.findByFriendsRequestCode(anyString())).willReturn(Optional.of(recipient));
        given(friendRequestRepository.isFriendRequestAlreadyExists(any(), any())).willReturn(false);
        given(friendRequestRepository.save(any(FriendRequest.class))).willAnswer(returnsFirstArg());

        // when
        var result = friendRequestService.createNewFriendRequest(randomUUID().toString(), friendRequestCode);

        // then
        assertThat(result.getSender(), is(sender));
        assertThat(result.getRecipient(), is(recipient));
        assertThat(result.getSentTime(), is(notNullValue()));
        verify(friendRequestRepository, times(1)).save(any(FriendRequest.class));
    }

    @Test
    public void exceptionShouldBeThrownWhenNotFoundSender() {
        // given
        given(chatProfileRepository.findById(any(UUID.class))).willReturn(Optional.empty());

        // when + then
        assertThrows(NotFoundException.class, () -> friendRequestService.createNewFriendRequest(randomUUID().toString(),
                ""));
        verify(friendRequestRepository, times(0)).save(any(FriendRequest.class));
    }

    @Test
    public void exceptionShouldBeThrownWhenNotExistsChatProfileWithFriendRequestCode() {
        // given
        given(chatProfileRepository.findById(any(UUID.class))).willReturn(Optional.of(new ChatProfile()));
        given(chatProfileRepository.findByFriendsRequestCode(anyString())).willReturn(Optional.empty());

        // when + then
        assertThrows(NotFoundException.class,
                () -> friendRequestService.createNewFriendRequest(randomUUID().toString(), ""));
        verify(friendRequestRepository, times(0)).save(any(FriendRequest.class));
    }

    @Test
    public void exceptionShouldBeThrownWhenSendFriendRequestToYourself() {

        // given
        var sender = new ChatProfile();
        var friendsRequestCode = "test";
        sender.setFriendsRequestCode(friendsRequestCode);
        given(chatProfileRepository.findById(any(UUID.class))).willReturn(Optional.of(sender));
        given(chatProfileRepository.findByFriendsRequestCode(anyString())).willReturn(Optional.of(new ChatProfile()));

        // when + then
        assertThrows(InvalidDataException.class,
                () -> friendRequestService.createNewFriendRequest(randomUUID().toString(), friendsRequestCode));
        verify(friendRequestRepository, times(0)).save(any(FriendRequest.class));
    }


    @Test
    public void exceptionShouldBeThrownWhenFriendRequestAlreadyExists() {

        // given
        var friendsRequestCode = "test";
        var sender = new ChatProfile();
        sender.setFriendsRequestCode(friendsRequestCode);
        given(chatProfileRepository.findById(any(UUID.class))).willReturn(Optional.of(sender));
        given(chatProfileRepository.findByFriendsRequestCode(anyString())).willReturn(Optional.of(new ChatProfile()));
        given(friendRequestRepository.isFriendRequestAlreadyExists(any(ChatProfile.class), any(ChatProfile.class)))
                .willReturn(true);

        // when + then
        assertThrows(AlreadyExistsException.class,
                () -> friendRequestService.createNewFriendRequest(randomUUID().toString(), ""));
        verify(friendRequestRepository, times(0)).save(any(FriendRequest.class));
    }


    @Test
    public void exceptionShouldBeThrownWhenReplyToNotExistingFriendRequest() {

        // given
        given(friendRequestRepository.findById(any(Long.class))).willReturn(Optional.empty());

        // when + then
        assertThrows(NotFoundException.class,
                () -> friendRequestService.replyToFriendRequest(1, "", false));
        verify(friendRequestRepository, times(0)).save(any(FriendRequest.class));
        verify(friendRequestRepository, times(0)).delete(any(FriendRequest.class));
        verify(friendChatService, times(0))
                .createFriendChat(any(ChatProfile.class), any(ChatProfile.class));
    }

    @Test
    public void exceptionShouldBeThrownWhenReplyToAlreadyAcceptedFriendRequest() {

        // given
        var friendRequest = new FriendRequest();
        friendRequest.setAccepted(true);
        given(friendRequestRepository.findById(any(Long.class))).willReturn(Optional.of(friendRequest));

        // when + then
        assertThrows(InvalidDataException.class,
                () -> friendRequestService.replyToFriendRequest(1, "", false));
        verify(friendRequestRepository, times(0)).save(any(FriendRequest.class));
        verify(friendRequestRepository, times(0)).delete(any(FriendRequest.class));
        verify(friendChatService, times(0))
                .createFriendChat(any(ChatProfile.class), any(ChatProfile.class));
    }

    @Test
    public void exceptionShouldBeThrownWhenNotRecipientReplyToFriendRequest() {
        // given
        var recipient = new ChatProfile();
        recipient.setUserId(randomUUID());
        var friendRequest = new FriendRequest();
        friendRequest.setAccepted(false);
        friendRequest.setRecipient(recipient);
        given(friendRequestRepository.findById(any(Long.class))).willReturn(Optional.of(friendRequest));

        // when + then
        assertThrows(InvalidDataException.class,
                () -> friendRequestService.replyToFriendRequest(1, randomUUID().toString(), false));
        verify(friendRequestRepository, times(0)).save(any(FriendRequest.class));
        verify(friendRequestRepository, times(0)).delete(any(FriendRequest.class));
        verify(friendChatService, times(0))
                .createFriendChat(any(ChatProfile.class), any(ChatProfile.class));
    }

    @Test
    public void friendChatShouldBeCreatedWhenAcceptFriendRequest() {

        // given
        var recipient = new ChatProfile();
        recipient.setUserId(randomUUID());

        var sender = new ChatProfile();
        sender.setUserId(randomUUID());

        var friendRequest = new FriendRequest();
        friendRequest.setAccepted(false);
        friendRequest.setRecipient(recipient);
        friendRequest.setSender(sender);

        given(friendRequestRepository.findById(1L)).willReturn(Optional.of(friendRequest));
        given(friendRequestRepository.save(any(FriendRequest.class))).willAnswer(returnsFirstArg());
        given(friendRequestRepository.getOne(1L)).willReturn(friendRequest);

        // when
        friendRequestService.replyToFriendRequest(1, recipient.getUserId().toString(), true);

        // then
        assertThat(friendRequestRepository.getOne(1L).isAccepted(), is(true));
        verify(friendRequestRepository, times(1)).save(friendRequest);
        verify(friendChatService, times(1)).createFriendChat(sender, recipient);
    }

    @Test
    public void friendRequestShouldBeDeletedWhenReject() {

        // given
        var recipient = new ChatProfile();
        recipient.setUserId(randomUUID());

        var sender = new ChatProfile();
        sender.setUserId(randomUUID());

        var friendRequest = new FriendRequest();
        friendRequest.setAccepted(false);
        friendRequest.setRecipient(recipient);
        friendRequest.setSender(sender);

        given(friendRequestRepository.findById(1L)).willReturn(Optional.of(friendRequest));

        // when
        friendRequestService.replyToFriendRequest(1, recipient.getUserId().toString(), false);


        // then
        verify(friendRequestRepository, times(1)).delete(friendRequest);
        verify(friendRequestRepository, times(0)).save(friendRequest);
        verify(friendChatService, times(0))
                .createFriendChat(friendRequest.getSender(), friendRequest.getRecipient());
    }


    @Test
    public void exceptionShouldBeThrownWhenDeleteNotExistingFriendRequest() {

        // given
        given(friendRequestRepository.findById(1L)).willReturn(Optional.empty());

        // when + then
        assertThrows(NotFoundException.class,
                () -> friendRequestService.deleteFriendRequestBySender(randomUUID().toString(), 1));
        verify(friendRequestRepository, times(0)).delete(any(FriendRequest.class));
    }

    @Test
    public void exceptionShouldBeThrownWhenDeleteFriendRequestByNotSender() {

        // given
        var sender = new ChatProfile();
        sender.setUserId(randomUUID());

        var friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        given(friendRequestRepository.findById(1L)).willReturn(Optional.of(friendRequest));

        // when + then
        assertThrows(InvalidDataException.class,
                () -> friendRequestService.deleteFriendRequestBySender(randomUUID().toString(), 1));
        verify(friendRequestRepository, times(0)).delete(friendRequest);
    }

    @Test
    public void exceptionShouldBeThrownWhenDeleteAlreadyAcceptedFriendRequest() {
        // given
        var sender = new ChatProfile();
        sender.setUserId(randomUUID());

        var acceptedFriendRequest = new FriendRequest();
        acceptedFriendRequest.setSender(sender);
        acceptedFriendRequest.setAccepted(true);

        given(friendRequestRepository.findById(1L)).willReturn(Optional.of(acceptedFriendRequest));

        // when + then
        assertThrows(InvalidDataException.class,
                () -> friendRequestService.deleteFriendRequestBySender(acceptedFriendRequest.getSender().getUserId().toString(), 1));
        verify(friendRequestRepository, times(0)).delete(acceptedFriendRequest);
    }

    @Test
    public void friendRequestShouldBeDeleted(){
        // given
        var sender = new ChatProfile();
        sender.setUserId(randomUUID());

        var friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setAccepted(false);

        given(friendRequestRepository.findById(1L)).willReturn(Optional.of(friendRequest));

        // when
        friendRequestService.deleteFriendRequestBySender(sender.getUserId().toString(),1);

        // then
        verify(friendRequestRepository,times(1)).delete(friendRequest);

    }






}
