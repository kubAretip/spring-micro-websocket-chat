package pl.kubaretip.chatservice.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kubaretip.chatservice.domain.ChatProfile;
import pl.kubaretip.chatservice.repository.ChatProfileRepository;
import pl.kubaretip.exceptionutils.InvalidDataException;
import pl.kubaretip.exceptionutils.NotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ChatProfileServiceImplTest {

    @Mock
    private ChatProfileRepository chatProfileRepository;

    @InjectMocks
    private ChatProfileServiceImpl chatProfileService;

    @Test
    public void exceptionShouldBeThrownWhenCreateChatProfileWithEmptyUserId() {
        // given + when + then
        assertThrows(InvalidDataException.class, () -> {
            chatProfileService.createChatProfile("", null);
        });
    }

    @Test
    public void exceptionShouldBeThrownWhenCreateChatProfileWithNullUserId() {
        // given + when + then
        assertThrows(InvalidDataException.class, () -> {
            chatProfileService.createChatProfile(null, null);
        });
    }

    @Test
    public void exceptionShouldBeThrownWhenCreateChatProfileWithEmptyUsername() {
        // given + when + then
        assertThrows(InvalidDataException.class, () -> {
            chatProfileService.createChatProfile("id", "");
        });
    }

    @Test
    public void exceptionShouldBeThrownWhenCreateChatProfileWithNullUsername() {
        // given + when + then
        assertThrows(InvalidDataException.class, () -> {
            chatProfileService.createChatProfile("id", null);
        });
    }

    @Test
    public void shouldCreateNewChatProfile() {
        // given
        var userId = "44e22efe-ed49-470a-99f0-f0bfc698629b";
        var username = "username";
        given(chatProfileRepository.save(any(ChatProfile.class))).willAnswer(returnsFirstArg());

        // when
        var result = chatProfileService.createChatProfile(userId, username);

        // then
        assertThat(result.getUserId().toString(), is(equalTo(userId)));
        // assertThat(result.getFriendsRequestCode(), is(equalTo(username)));

        verify(chatProfileRepository, times(1)).save(any());
    }


    @Test
    public void friendsRequestCodeForNewChatProfileShouldStartWithUsername() {
        // given
        var userId = "44e22efe-ed49-470a-99f0-f0bfc698629b";
        var username = "username";
        given(chatProfileRepository.save(any(ChatProfile.class))).willAnswer(returnsFirstArg());

        // when
        var result = chatProfileService.createChatProfile(userId, username);

        // then
        assertThat(result.getFriendsRequestCode(), is(notNullValue()));
        assertThat(result.getFriendsRequestCode(), startsWith(username));
        verify(chatProfileRepository, times(1)).save(any());
    }

    @Test
    public void shouldRegenerateFriendCode() {
        // given
        var userId = "44e22efe-ed49-470a-99f0-f0bfc698629b";
        var username = "test";
        var friendsRequestCode = "123";
        var chatProfile = new ChatProfile();
        chatProfile.setUserId(UUID.fromString(userId));
        chatProfile.setFriendsRequestCode(friendsRequestCode);
        given(chatProfileRepository.findById(any(UUID.class))).willReturn(Optional.of(chatProfile));
        given(chatProfileRepository.save(any(ChatProfile.class))).willAnswer(returnsFirstArg());

        // when
        var result = chatProfileService.generateNewFriendsRequestCode(userId, username);

        // then
        assertThat(result.getFriendsRequestCode(), startsWith(username));
        assertThat(result.getFriendsRequestCode(), is(not(equalTo(friendsRequestCode))));
        verify(chatProfileRepository, times(1)).save(any());

    }


    @Test
    public void shouldReturnChatProfileById() {

        // given
        given(chatProfileRepository.findById(any(UUID.class))).willReturn(Optional.of(new ChatProfile()));

        // when
        var result = chatProfileService.getChatProfileById("44e22efe-ed49-470a-99f0-f0bfc698629b");

        // then
        assertThat(result, is(notNullValue()));
        verify(chatProfileRepository, times(1)).findById(any());
    }

    @Test
    public void exceptionShouldBeThrownWhereNotFoundUserById() {

        // given
        given(chatProfileRepository.findById(any(UUID.class))).willReturn(Optional.empty());

        // when + then
        assertThrows(NotFoundException.class,
                () -> chatProfileService.getChatProfileById("44e22efe-ed49-470a-99f0-f0bfc698629b"));
    }


}
