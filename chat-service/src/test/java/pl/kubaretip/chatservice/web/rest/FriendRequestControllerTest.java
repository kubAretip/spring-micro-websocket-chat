package pl.kubaretip.chatservice.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import pl.kubaretip.chatservice.SpringSecurityUserDetailsTestConfig;
import pl.kubaretip.chatservice.domain.FriendRequest;
import pl.kubaretip.chatservice.dto.FriendRequestDTO;
import pl.kubaretip.chatservice.dto.mapper.FriendRequestMapper;
import pl.kubaretip.chatservice.service.FriendRequestService;

import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.kubaretip.chatservice.SpringSecurityUserDetailsTestConfig.MOCK_USER_ID;

@ContextConfiguration(classes = {SpringSecurityUserDetailsTestConfig.class})
@WebMvcTest(controllers = FriendRequestController.class)
public class FriendRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FriendRequestService friendRequestService;

    @MockBean
    private FriendRequestMapper friendRequestMapper;

    @WithUserDetails(userDetailsServiceBeanName = "testUserDetailsService")
    @Test
    public void shouldReturn201WhenCreateNewFriendRequest() throws Exception {

        // given
        var friendRequestDTO = new FriendRequestDTO();
        friendRequestDTO.setId(1L);
        friendRequestDTO.setSentTime("date");
        var friendRequest = new FriendRequest();
        friendRequest.setId(1L);
        given(friendRequestService.createNewFriendRequest(anyString(), anyString())).willReturn(friendRequest);
        given(friendRequestMapper.mapToFriendRequestDTO(any())).willReturn(friendRequestDTO);

        // when
        var request = post("/friend-requests")
                .param("invite_code", "fake_code");
        // then
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("sentTime", is(equalTo("date"))));
        then(friendRequestService).should(times(1))
                .createNewFriendRequest(MOCK_USER_ID, "fake_code");
        then(friendRequestMapper).should(times(1))
                .mapToFriendRequestDTO(any());
    }

    @WithUserDetails(userDetailsServiceBeanName = "testUserDetailsService")
    @Test
    public void shouldReturn204WhenReplyToFriendRequest() throws Exception {

        // given
        willDoNothing().given(friendRequestService).replyToFriendRequest(1, MOCK_USER_ID, true);
        // when
        var request = patch("/friend-requests/{id}", 1)
                .param("accept", "true");
        // then
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
        then(friendRequestService).should(times(1))
                .replyToFriendRequest(1, MOCK_USER_ID, true);
    }

    @WithUserDetails(userDetailsServiceBeanName = "testUserDetailsService")
    @Test
    public void shouldReturn204WhenDeleteFriendRequestById() throws Exception {

        // given
        willDoNothing().given(friendRequestService).deleteFriendRequestBySender(MOCK_USER_ID, 1);

        // when
        var request = delete("/friend-requests/{id}", 1);

        // then
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
        then(friendRequestService).should(times(1))
                .deleteFriendRequestBySender(MOCK_USER_ID, 1);
    }

    @WithUserDetails(userDetailsServiceBeanName = "testUserDetailsService")
    @Test
    public void shouldReturn200WhenGetNotAcceptedRecipientFriendRequests() throws Exception {

        // given
        given(friendRequestMapper.mapToFriendRequestDTOListWithoutRecipient(anyList()))
                .willReturn(Collections.singletonList(new FriendRequestDTO()));
        // when
        var request = get("/friend-requests/received");

        // then
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
        then(friendRequestService).should(times(1))
                .getAllNotAcceptedFriendRequestsByRecipientId(MOCK_USER_ID);
    }

    @WithUserDetails(userDetailsServiceBeanName = "testUserDetailsService")
    @Test
    public void shouldReturn200WhenGetNotAcceptedSenderFriendRequests() throws Exception {

        // given
        given(friendRequestMapper.mapToFriendRequestDTOListWithoutSender(anyList()))
                .willReturn(Collections.singletonList(new FriendRequestDTO()));
        // when
        var request = get("/friend-requests/sent");

        // then
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
        then(friendRequestService).should(times(1))
                .getAllNotAcceptedFriendRequestsBySenderId(MOCK_USER_ID);
    }


}
