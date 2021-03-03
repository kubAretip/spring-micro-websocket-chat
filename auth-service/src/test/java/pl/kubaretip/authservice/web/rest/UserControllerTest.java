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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.kubaretip.authservice.domain.User;
import pl.kubaretip.authservice.mapper.UserMapper;
import pl.kubaretip.authservice.messaging.sender.UserSender;
import pl.kubaretip.authservice.service.UserService;
import pl.kubaretip.authservice.utils.JacksonIgnoreWriteOnlyAccess;
import pl.kubaretip.authservice.utils.SpringSecurityWebTestConfig;
import pl.kubaretip.authservice.web.model.ChangePassRequest;
import pl.kubaretip.dtomodels.UserDTO;
import pl.kubaretip.exceptionutils.AlreadyExistsException;
import pl.kubaretip.exceptionutils.InvalidDataException;
import pl.kubaretip.exceptionutils.NotFoundException;

import java.util.stream.Stream;

import static java.util.UUID.randomUUID;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    public void getUserByIdShouldReturns404WhenUserNotFound() throws Exception {

        // given
        given(userService.findUserById(anyString())).willThrow(NotFoundException.class);

        // when + then
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", randomUUID().toString()))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser
    public void getUserByIdShouldReturns200WhenUserExists() throws Exception {
        // given
        var id = randomUUID().toString();
        var userDTO = new UserDTO();
        userDTO.setId(id);
        given(userService.findUserById(id)).willReturn(new User());
        given(userMapper.mapToUserDTO(any(User.class))).willReturn(userDTO);

        // when + then
        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(is(equalTo(id))));
        verify(userService, times(1)).findUserById(id);
        verify(userMapper, times(1)).mapToUserDTO(any(User.class));
    }

    @WithMockUser
    @Test
    public void editUserShouldReturns400WhenUserIdIsIncorrect() throws Exception {

        // given
        var userDTO = new UserDTO();
        userDTO.setFirstName("name");
        userDTO.setLastName("surname");
        given(userService.modifyUser(anyString(), anyString(), anyString())).willThrow(InvalidDataException.class);

        // when + then
        mockMvc.perform(patch("/users/{id}", randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser
    @Test
    public void editUserShouldReturns200WhenSuccessfullyEdited() throws Exception {

        // given
        given(userService.modifyUser(anyString(), anyString(), anyString())).willReturn(new User());
        given(userMapper.mapToUserDTO(any(User.class))).willReturn(new UserDTO());

        // when + then
        mockMvc.perform(patch("/users/{id}", randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UserDTO())))
                .andExpect(status().isOk());

    }


    @WithUserDetails(userDetailsServiceBeanName = "customUserDetailsService", value = "testUser")
    @Test
    public void changePasswordShouldReturns204WhenSuccessfullyChanged() throws Exception {
        // given (userId comes from userDetailsService)
        var userId = "1062d618-64f6-401c-ab7c-ef050eb6f4b2";
        var changePassRequest = new ChangePassRequest();
        var currentPassword = "1234567";
        var newPassword = "7654321";
        changePassRequest.setCurrentPassword(currentPassword);
        changePassRequest.setNewPassword(newPassword);

        willDoNothing().given(userService).changeUserPassword(userId, currentPassword, newPassword);

        // when + then
        mockMvc.perform(patch("/users/{id}/change-password", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePassRequest)))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).changeUserPassword(userId, currentPassword, newPassword);
    }

    @WithUserDetails(userDetailsServiceBeanName = "customUserDetailsService", value = "testUser")
    @Test
    public void changePasswordShouldReturns400WhenIncorrectUserId() throws Exception {
        // given
        var userId = "wrong id";
        var request = new ChangePassRequest();
        request.setNewPassword("fake_password");
        request.setCurrentPassword("fake_password");

        // when + then
        mockMvc.perform(patch("/users/{id}/change-password", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        verify(userService, times(0)).changeUserPassword(any(), any(), any());
    }

    @ParameterizedTest
    @MethodSource("invalidChangePasswordRequestsSource")
    @WithMockUser
    public void changePasswordShouldReturns400WhenInvalidInput(ChangePassRequest request) throws Exception {

        // given (in method source)
        // when + then
        mockMvc.perform(patch("/users/{id}/change-password", "fakeId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        verify(userService, times(0)).changeUserPassword(any(), any(), any());
    }

    private static Stream<Arguments> invalidChangePasswordRequestsSource() {
        return Stream.of(
                Arguments.of(new ChangePassRequest("", "")),
                Arguments.of(new ChangePassRequest(null, null)),
                Arguments.of(new ChangePassRequest("1", "1")),
                Arguments.of(new ChangePassRequest("password", "1")),
                Arguments.of(new ChangePassRequest("1", "password"))
        );
    }


}
