package pl.kubaretip.chatmessagesservice.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kubaretip.chatmessagesservice.constant.MessageStatus;
import pl.kubaretip.chatmessagesservice.document.ChatMessage;
import pl.kubaretip.chatmessagesservice.repository.ChatMessageRepository;
import pl.kubaretip.exceptionutils.InvalidDataException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class ChatMessageServiceImplTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @InjectMocks
    private ChatMessageServiceImpl chatMessageService;

    @Test
    void shouldSaveMessage() {

        // given
        var newChatMessage = new ChatMessage();
        newChatMessage.setId("1");
        newChatMessage.setStatus(MessageStatus.RECEIVED);
        given(chatMessageRepository.save(any())).willReturn(Mono.just(newChatMessage));

        // when
        var saveChatMessage = chatMessageService.saveChatMessage(1L, "sender",
                "recipient", "content", "2021-03-20T13:21:45.000Z");

        // then
        StepVerifier.create(saveChatMessage)
                .assertNext(chatMessage -> {
                    assertThat(chatMessage.getStatus(), is(equalTo(MessageStatus.RECEIVED)));
                    assertThat(chatMessage.getId(), is("1"));
                })
                .verifyComplete();
        verify(chatMessageRepository, times(1)).save(any(ChatMessage.class));
    }

    @Test
    void exceptionShouldBeThrownWhenSaveMessageWithNullFriendChat() {
        // given + when + then
        StepVerifier.create(chatMessageService.saveChatMessage(null, "sender", "recipient",
                "content", "2021-03-20T13:21:45.000Z"))
                .expectError()
                .verifyThenAssertThat()
                .hasOperatorErrorOfType(InvalidDataException.class);
        verify(chatMessageRepository, times(0)).save(any(ChatMessage.class));
    }

    @Test
    void exceptionShouldBeThrownWhenSaveMessageWithEmptyContent() {
        // given + when + then
        StepVerifier.create(chatMessageService.saveChatMessage(1L, "sender", "recipient",
                "", "2021-03-20T13:21:45.000Z"))
                .expectError()
                .verifyThenAssertThat()
                .hasOperatorErrorOfType(InvalidDataException.class);
        verify(chatMessageRepository, times(0)).save(any(ChatMessage.class));
    }

    @Test
    void exceptionShouldBeThrownWhenSaveMessageWithEmptySender() {
        // given + when + then
        StepVerifier.create(chatMessageService.saveChatMessage(1L, "", "recipient",
                "content", "2021-03-20T13:21:45.000Z"))
                .expectError()
                .verifyThenAssertThat()
                .hasOperatorErrorOfType(InvalidDataException.class);
        verify(chatMessageRepository, times(0)).save(any(ChatMessage.class));
    }

    @Test
    void exceptionShouldBeThrownWhenSaveMessageWithEmptyRecipient() {
        // given + when + then
        StepVerifier.create(chatMessageService.saveChatMessage(1L, "sender", "",
                "content", "2021-03-20T13:21:45.000Z"))
                .expectError()
                .verifyThenAssertThat()
                .hasOperatorErrorOfType(InvalidDataException.class);
        verify(chatMessageRepository, times(0)).save(any(ChatMessage.class));
    }

    @Test
    void exceptionShouldBeThrownWhenSaveMessageWithTimeInNotUTCFormat() {
        // given + when + then
        StepVerifier.create(chatMessageService.saveChatMessage(1L, "sender", "recipient",
                "content", "2021.03.20 13:21"))
                .expectError()
                .verifyThenAssertThat()
                .hasOperatorErrorOfType(InvalidDataException.class);
        verify(chatMessageRepository, times(0)).save(any(ChatMessage.class));
    }


    @Test
    void exceptionShouldBeThrownWhenFindMessagesWithInvalidDateFormat() {
        // given + when + then
        StepVerifier.create(chatMessageService.findLastUsersMessagesFromTime(0, 0,
                "2021.03.14 12:00", 0))
                .expectError()
                .verifyThenAssertThat()
                .hasOperatorErrorOfType(InvalidDataException.class);
        verify(chatMessageRepository, times(0))
                .findByTimeLessThanAndFriendChatIn(any(), any(), any());
    }

    @Test
    void shouldMarkCurrentUserMessagesAsDeliveredByFriendChatId() {

        // given
        var chatMessage = Mockito.mock(ChatMessage.class);
        chatMessage.setId("id1");
        chatMessage.setFriendChat(1L);
        given(chatMessageRepository.findByFriendChatAndRecipientAndStatus(1, "currentUserID", MessageStatus.RECEIVED))
                .willReturn(Flux.just(chatMessage, chatMessage));
        given(chatMessageRepository.save(chatMessage)).willReturn(Mono.just(chatMessage));

        // when + then
        StepVerifier.create(chatMessageService.setDeliveredStatusForAllRecipientMessagesInFriendChat(1, "currentUserID"))
                .verifyComplete();
        verify(chatMessageRepository, times(1))
                .findByFriendChatAndRecipientAndStatus(1, "currentUserID", MessageStatus.RECEIVED);
        verify(chatMessageRepository, times(2)).save(chatMessage);
        verify(chatMessage, times(2)).setStatus(MessageStatus.DELIVERED);
    }
}
