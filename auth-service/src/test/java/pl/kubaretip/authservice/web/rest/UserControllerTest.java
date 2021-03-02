package pl.kubaretip.authservice.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.kubaretip.authservice.domain.User;
import pl.kubaretip.authservice.mapper.UserMapper;
import pl.kubaretip.authservice.messaging.sender.UserSender;
import pl.kubaretip.authservice.service.UserService;
import pl.kubaretip.authservice.utils.JacksonIgnoreWriteOnlyAccess;
import pl.kubaretip.authservice.utils.SpringSecurityWebTestConfig;
import pl.kubaretip.dtomodels.UserDTO;
import pl.kubaretip.exceptionutils.AlreadyExistsException;
import pl.kubaretip.exceptionutils.InvalidDataException;
import pl.kubaretip.exceptionutils.NotFoundException;

import java.util.stream.Stream;

import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(controllers = UserController.class)
@ContextConfiguration(classes = SpringSecurityWebTestConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserSender userSender;


    @BeforeAll
    public void setup() {
        // ignore write only access in password field in UserDTO class
        objectMapper.setAnnotationIntrospector(new JacksonIgnoreWriteOnlyAccess());
    }

    @WithAnonymousUser
    @Test
    public void creatingNewUserShouldReturns200WhenValidInput() throws Exception {
        //given
        var inputUser = new UserDTO("username", "password123", "Jan", "Smith", "test@example.com");

        given(userService.createUser(any(), any(), any(), any(), any())).willReturn(new User());
        given(userMapper.mapToUserDTO(any())).willReturn(new UserDTO());

        //when + then
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(inputUser)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
        verify(userService, times(1)).createUser(any(), any(), any(), any(), any());
        verify(userSender, times(1)).send(any(UserDTO.class));
    }

    @WithAnonymousUser
    @ParameterizedTest
    @MethodSource("invalidNewUsersSource")
    public void creatingNewUserShouldReturns400WhenInvalidInput(UserDTO input) throws Exception {
        // given (in method source)
        // when + then
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
        verify(userService, times(0)).createUser(any(), any(), any(), any(), any());
        verify(userSender, times(0)).send(any(UserDTO.class));
    }

    private static Stream<Arguments> invalidNewUsersSource() {
        return Stream.of(
                Arguments.of(new UserDTO("", "", "", "", "")),
                Arguments.of(new UserDTO(null, null, null, null, null)),
                Arguments.of(new UserDTO("t", "12", "@dsa", "@x-1", "email@.pl")),
                Arguments.of(new UserDTO("username", "password", "s", "s", "email@example.com")),
                Arguments.of(new UserDTO("userna_2@me", "password", "aaaa", "ssss", "email@example.com"))
        );
    }

    @WithAnonymousUser
    @Test
    public void creatingNewUserShouldReturns400WhenServiceThrowInvalidDataException() throws Exception {
        // given
        var validNewUser = new UserDTO("username", "password123", "Jan", "Smith", "test@example.com");
        given(userService.createUser(any(), any(), any(), any(), any())).willThrow(new InvalidDataException(""));
        // when + then
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(validNewUser)))
                .andExpect(status().isBadRequest());
        verify(userService, times(1)).createUser(any(), any(), any(), any(), any());
    }

    @WithAnonymousUser
    @Test
    public void creatingNewUserShouldReturns409WhenServiceThrownAlreadyExists() throws Exception {
        // given
        var validNewUser = new UserDTO("username", "password123", "Jan", "Smith", "test@example.com");
        given(userService.createUser(any(), any(), any(), any(), any())).willThrow(new AlreadyExistsException(""));
        // when + then
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(validNewUser)))
                .andExpect(status().isConflict());
        verify(userService, times(1)).createUser(any(), any(), any(), any(), any());
    }

    @WithAnonymousUser
    @Test
    public void userActivationShouldReturns400WhenServiceThrownInvalidDate() throws Exception {
        // given
        var activationKey = "test";
        willThrow(new InvalidDataException("error")).given(userService).activateUser(activationKey);

        // when + then
        mockMvc.perform(patch("/users/activate")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("data", activationKey))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).activateUser(activationKey);
    }

    @WithAnonymousUser
    @Test
    public void userActivationShouldReturns204WhenActivationSuccess() throws Exception {
        // given
        var activationKey = "test";
        willDoNothing().given(userService).activateUser(activationKey);
        // when + then
        mockMvc.perform(patch("/users/activate")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("data", activationKey))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).activateUser(activationKey);
    }

    @Test
    @WithMockUser
    public void getUserByIdShouldReturns404WhenUserNotFount() throws Exception {

        // given
        given(userService.findUserById(anyString())).willThrow(NotFoundException.class);

        // when + then
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", randomUUID().toString()))
                .andExpect(status().isNotFound());
    }


}
