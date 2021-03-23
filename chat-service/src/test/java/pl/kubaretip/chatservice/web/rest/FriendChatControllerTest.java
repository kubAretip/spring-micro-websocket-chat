package pl.kubaretip.chatservice.web.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import pl.kubaretip.chatservice.SpringSecurityUserDetailsTestConfig;
import pl.kubaretip.chatservice.dto.ChatProfileDTO;
import pl.kubaretip.chatservice.dto.FriendChatDTO;
import pl.kubaretip.chatservice.dto.mapper.FriendChatMapper;
import pl.kubaretip.chatservice.messaging.sender.DeleteMessagesSender;
import pl.kubaretip.chatservice.service.FriendChatService;
import pl.kubaretip.exceptionutils.NotFoundException;

import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.kubaretip.chatservice.SpringSecurityUserDetailsTestConfig.MOCK_USER_ID;

@ContextConfiguration(classes = {SpringSecurityUserDetailsTestConfig.class})
@WebMvcTest(controllers = FriendChatController.class)
public class FriendChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FriendChatService friendChatService;

    @MockBean
    private FriendChatMapper friendChatMapper;

    @MockBean
    private DeleteMessagesSender deleteMessagesSender;


    @WithUserDetails(userDetailsServiceBeanName = "testUserDetailsService")
    @Test
    public void shouldReturn200WhenGetAllFriendsChats() throws Exception {

        // given
        var fakeFriendChatDTO = new FriendChatDTO();
        var fakeRecipient = new ChatProfileDTO();
        fakeFriendChatDTO.setId(1L);
        fakeFriendChatDTO.setChatWith(2L);
        fakeFriendChatDTO.setRecipient(fakeRecipient);
        fakeRecipient.setUserId("fake_recipient_id");

        given(friendChatMapper.mapToFriendChatList(any())).willReturn(Collections.singletonList(fakeFriendChatDTO));

        // when
        var request = get("/friends-chats")
                .content(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].chatWith", is(2)))
                .andExpect(jsonPath("$[0].recipient.userId", is(equalTo("fake_recipient_id"))));
        then(friendChatMapper).should(times(1)).mapToFriendChatList(anyList());
        then(friendChatService).should(times(1)).getAllFriendsChatsBySender(anyString());
    }

    @WithUserDetails(userDetailsServiceBeanName = "testUserDetailsService")
    @Test
    public void shouldReturn204WhenDeleteUserFriendChat() throws Exception {

        // given
        // when
        var request = delete("/friends-chats")
                .param("friend_chat", String.valueOf(1))
                .param("friend_chat_with", String.valueOf(2));
        // then
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
        then(friendChatService).should(times(1))
                .deleteFriendChat(1, 2, MOCK_USER_ID);
        then(deleteMessagesSender).should(times(1)).sendDeletingMessagesTask(List.of(1L, 2L));
    }

    @WithUserDetails(userDetailsServiceBeanName = "testUserDetailsService")
    @Test
    public void shouldReturn404WhenDeleteNotExistedFriendChat() throws Exception {

        // given
        willThrow(NotFoundException.class).given(friendChatService).deleteFriendChat(anyLong(), anyLong(), anyString());

        // when
        var request = delete("/friends-chats")
                .param("friend_chat", String.valueOf(1))
                .param("friend_chat_with", String.valueOf(2));
        // then
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
        then(friendChatService).should(times(1))
                .deleteFriendChat(1, 2, MOCK_USER_ID);
    }

}
