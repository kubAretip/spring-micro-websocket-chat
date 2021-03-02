package pl.kubaretip.chatservice.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kubaretip.chatservice.domain.ChatProfile;
import pl.kubaretip.chatservice.domain.FriendChat;
import pl.kubaretip.chatservice.repository.ChatProfileRepository;
import pl.kubaretip.chatservice.repository.FriendChatRepository;
import pl.kubaretip.chatservice.repository.FriendRequestRepository;
import pl.kubaretip.exceptionutils.AlreadyExistsException;
import pl.kubaretip.exceptionutils.NotFoundException;

import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FriendChatServiceImplTest {

    @Mock
    private FriendChatRepository friendChatRepository;

    @Mock
    private ChatProfileRepository chatProfileRepository;

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @InjectMocks
    private FriendChatServiceImpl friendChatService;


    @Test
    public void exceptionShouldBeThrownWhenFriendChatAlreadyExists() {

        // given
        given(friendChatRepository.existsFriendChatForUsers(any(ChatProfile.class), any(ChatProfile.class)))
                .willReturn(true);

        // when + then
        assertThrows(AlreadyExistsException.class,
                () -> friendChatService.createFriendChat(new ChatProfile(), new ChatProfile()));

    }

    @Test
    public void shouldCreateNewFriendChat() {

        //given
        var chatProfile1 = new ChatProfile();
        var chatProfile2 = new ChatProfile();
        given(friendChatRepository.existsFriendChatForUsers(any(ChatProfile.class), any(ChatProfile.class)))
                .willReturn(false);
        given(friendChatRepository.save(any(FriendChat.class))).willAnswer(returnsFirstArg());

        // when
        friendChatService.createFriendChat(chatProfile1, chatProfile2);

        // then
        verify(friendChatRepository, times(4)).save(any(FriendChat.class));

    }


    @Test
    public void exceptionShouldBeThrownGetFriendsChatsByNotExistingUser() {
        // given
        given(chatProfileRepository.findById(any(UUID.class))).willReturn(Optional.empty());
        // when + then
        assertThrows(NotFoundException.class,
                () -> friendChatService.getAllFriendsChatsBySender(randomUUID().toString()));
    }

    @Test
    public void exceptionShouldBeThrownWhenDeleteNotExistingFriendChat() {

        // given
        given(friendChatRepository.findByIdAndFriendChatWithIdAndSenderId(anyLong(), anyLong(), any(UUID.class)))
                .willReturn(Optional.empty());

        // when + then
        assertThrows(NotFoundException.class,
                () -> friendChatService.deleteFriendChat(1, 2, randomUUID().toString()));
    }

    @Test
    public void shouldDeleteFriendChat() {

        // given
        var sender = new ChatProfile();
        var recipient = new ChatProfile();
        var friendChat = new FriendChat();
        friendChat.setSender(sender);
        friendChat.setRecipient(recipient);
        given(friendChatRepository.findByIdAndFriendChatWithIdAndSenderId(anyLong(), anyLong(), any(UUID.class)))
                .willReturn(Optional.of(friendChat));

        // when
        friendChatService.deleteFriendChat(1, 2, randomUUID().toString());

        // then
        verify(friendRequestRepository,times(1)).deleteFriendRequestByChatProfiles(sender,recipient);
        verify(friendChatRepository,times(1)).delete(friendChat);

    }


}
