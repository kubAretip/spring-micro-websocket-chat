package pl.kubaretip.chatservice.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.kubaretip.chatservice.SpringSecurityUserDetailsTestConfig;
import pl.kubaretip.chatservice.domain.ChatProfile;
import pl.kubaretip.chatservice.dto.ChatProfileDTO;
import pl.kubaretip.chatservice.dto.mapper.ChatProfileMapper;
import pl.kubaretip.chatservice.service.ChatProfileService;
import pl.kubaretip.exceptionutils.NotFoundException;

import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ContextConfiguration(classes = {SpringSecurityUserDetailsTestConfig.class})
@WebMvcTest(controllers = ChatProfileController.class)
public class ChatProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChatProfileService chatProfileService;

    @MockBean
    private ChatProfileMapper chatProfileMapper;


    @Test
    @WithMockUser
    public void shouldReturn200WhenChatProfileExists() throws Exception {

        // given
        var userId = randomUUID().toString();
        given(chatProfileService.getChatProfileById(userId)).willReturn(new ChatProfile());
        var chatProfileDTO = new ChatProfileDTO();
        chatProfileDTO.setUserId(userId);
        given(chatProfileMapper.chatProfileToChatProfileDTO(any(ChatProfile.class))).willReturn(chatProfileDTO);

        // when
        var request = MockMvcRequestBuilders.get("/chat-profiles/{id}", userId);

        // then
        mockMvc.perform(request.contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId").value(userId));
    }

    @Test
    @WithMockUser
    public void shouldReturn404WhenChatProfileNotExists() throws Exception {

        // given
        given(chatProfileService.getChatProfileById(anyString())).willThrow(NotFoundException.class);

        // when
        var request = MockMvcRequestBuilders.get("/chat-profiles/{id}", randomUUID().toString());

        // then
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @WithUserDetails(userDetailsServiceBeanName = "testUserDetailsService")
    @Test
    public void shouldReturn200WhenRenewFriendsRequestCode() throws Exception {

        // given
        var userId = SpringSecurityUserDetailsTestConfig.MOCK_USER_ID;
        var chatProfileDTO = new ChatProfileDTO();
        chatProfileDTO.setUserId(userId);
        chatProfileDTO.setFriendsRequestCode("fake_key");
        given(chatProfileService.generateNewFriendsRequestCode(anyString(), anyString())).willReturn(new ChatProfile());
        given(chatProfileMapper.chatProfileToChatProfileDTO(any(ChatProfile.class))).willReturn(chatProfileDTO);

        // when
        var request = MockMvcRequestBuilders.patch("/chat-profiles/{id}/new-friends-request-code", userId);

        // then
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId").value(userId))
                .andExpect(jsonPath("friendsRequestCode").value("fake_key"));
    }

    @WithUserDetails(userDetailsServiceBeanName = "testUserDetailsService")
    @Test
    public void shouldReturn400WhenRenewFriendsRequestCodeWithInvalidUserId() throws Exception {

        // given + when
        var request = MockMvcRequestBuilders.patch("/chat-profiles/{id}/new-friends-request-code", "invalid_id");

        // then
        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

}
