package pl.kubaretip.authservice.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.kubaretip.authservice.domain.Authority;
import pl.kubaretip.authservice.domain.User;
import pl.kubaretip.authservice.repository.AuthorityRepository;
import pl.kubaretip.authservice.repository.UserRepository;
import pl.kubaretip.authservice.service.UserService;
import pl.kubaretip.authutils.SecurityUtils;
import pl.kubaretip.authutils.security.SecurityUserDetails;
import pl.kubaretip.exceptionutils.AlreadyExistsException;
import pl.kubaretip.exceptionutils.InvalidDataException;
import pl.kubaretip.exceptionutils.NotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserServiceImpl.class})
public class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserService userService;


    @BeforeAll
    public static void setup() {
        var securityUserDetails = mock(SecurityUserDetails.class);
        var authentication = mock(Authentication.class);
        var securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        given(securityContext.getAuthentication()).willReturn(authentication);
        given(authentication.getPrincipal()).willReturn(securityUserDetails);
        given(securityContext.getAuthentication().getPrincipal()).willReturn(securityUserDetails);

        // mocker user id security context
        given(securityUserDetails.getId()).willReturn("123e4567-e89b-12d3-a456-426614174000");
    }


    @Test
    public void shouldCreateUser() {

        // given
        var username = "username123";
        var password = "secretPass1";
        var email = "test@example.com";
        var firstName = "john";
        var lastName = "smith";
        given(userRepository.existsByEmailIgnoreCase(anyString())).willReturn(false);
        given(userRepository.existsByUsernameIgnoreCase(anyString())).willReturn(false);
        given(authorityRepository.findByNameIgnoreCase(anyString())).willReturn(Optional.of(new Authority()));
        given(userRepository.save(any())).will(returnsFirstArg());

        // when
        userService.createUser(username, password, email, firstName, lastName);

        // then
        verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    public void newUserShouldHaveCapitalizeName() {
        // given
        var username = "username123";
        var password = "secretPass1";
        var email = "test@example.com";
        var firstName = "joHn";
        var lastName = "smItH";
        given(userRepository.existsByEmailIgnoreCase(anyString())).willReturn(false);
        given(userRepository.existsByUsernameIgnoreCase(anyString())).willReturn(false);
        given(authorityRepository.findByNameIgnoreCase(anyString())).willReturn(Optional.of(new Authority()));
        given(userRepository.save(any())).will(returnsFirstArg());

        // when
        var newUser = userService.createUser(username, password, email, firstName, lastName);

        // then
        assertThat(newUser.getFirstName(), is(equalTo("John")));
        assertThat(newUser.getLastName(), is(equalTo("Smith")));
    }

    @Test
    public void exceptionShouldBeThrownWhenAlreadyExistsUserWithEmail() {
        // given
        var username = "username123";
        var password = "secretPass1";
        var email = "test@example.com";
        var firstName = "joHn";
        var lastName = "smItH";
        given(userRepository.existsByEmailIgnoreCase(email)).willReturn(true);

        //when
        assertThrows(AlreadyExistsException.class, () -> userService.createUser(username, password, email, firstName,
                lastName));
    }

    @Test
    public void exceptionShouldBeThrownWhenAlreadyExistsUserWithUsername() {
        // given
        var username = "username123";
        var password = "secretPass1";
        var email = "test@example.com";
        var firstName = "joHn";
        var lastName = "smItH";
        given(userRepository.existsByUsernameIgnoreCase(username)).willReturn(true);
        //when
        assertThrows(AlreadyExistsException.class, () -> userService.createUser(username, password, email, firstName,
                lastName));
    }


    @Test
    void exceptionShouldBeThrownWhenUsernameIsNull() {
        // given
        String username = null;
        // when + then
        assertThrows(InvalidDataException.class, () -> userService.createUser(username,
                "", "", "", ""));
    }

    @Test
    void exceptionShouldBeThrownWhenUsernameIsEmpty() {
        // given
        var username = "";
        // when + then
        assertThrows(InvalidDataException.class, () -> userService.createUser(username,
                "", "", "", ""));
    }

    @Test
    void exceptionShouldBeThrownWhenUsernameIsBlank() {
        // given
        var username = "     ";
        // when + then
        assertThrows(InvalidDataException.class, () -> userService.createUser(username,
                "", "", "", ""));
    }

    @Test
    void exceptionShouldBeThrownWhenUsernameIsNotAlphanumeric() {
        // given
        var username = "user@.";
        // when + then
        assertThrows(InvalidDataException.class, () -> userService.createUser(username,
                "", "", "", ""));
    }

    @Test
    void exceptionShouldBeThrownWhenPasswordIsNull() {
        // given
        String password = null;
        // when + then
        assertThrows(InvalidDataException.class, () -> userService.createUser("username",
                password, "", "", ""));
    }

    @Test
    void exceptionShouldBeThrownWhenPasswordIsEmpty() {
        // given
        var password = "";
        // when + then
        assertThrows(InvalidDataException.class, () -> userService.createUser("username",
                password, "", "", ""));
    }

    @Test
    void exceptionShouldBeThrownWhenPasswordIsBlank() {
        // given
        var password = "     ";
        // when + then
        assertThrows(InvalidDataException.class, () -> userService.createUser("username",
                password, "", "", ""));
    }

    @Test
    void exceptionShouldBeThrownWhenPasswordLengthIsLessThenDeclaredInConstants() {
        // given
        var password = RandomStringUtils.randomAlphanumeric(5);
        // when + then
        assertThrows(InvalidDataException.class, () -> userService.createUser("username",
                password, "", "", ""));
    }

    @Test
    void exceptionShouldBeThrownWhenPasswordLengthIsGreaterThenDeclaredInConstants() {
        // given
        var password = RandomStringUtils.randomAlphanumeric(33);
        // when + then
        assertThrows(InvalidDataException.class, () -> userService.createUser("username",
                password, "", "", ""));
    }

    @Test
    public void exceptionShouldBeThrownWhenEmailIsNull() {
        // given
        String email = null;
        // when + then
        assertThrows(InvalidDataException.class, () -> userService.createUser("username", "password",
                email, "", ""));
    }

    @Test
    void exceptionShouldBeThrownWhenEmailIsInvalid() {
        // given
        var invalidEmail = "test@fd";
        var emptyEmail = "test";
        var blank = "  ";
        var empty = "";

        assertThrows(InvalidDataException.class, () -> {
            userService.createUser("username", "password", invalidEmail, "", "");
            userService.createUser("username", "password", emptyEmail, "", "");
            userService.createUser("username", "password", blank, "", "");
            userService.createUser("username", "password", empty, "", "");
        });
    }

    @Test
    void exceptionShouldThrownWhenFirstNameIsNull() {
        // given
        String firstName = null;
        // when + then
        assertThrows(InvalidDataException.class, () -> userService.createUser("username", "password",
                "email@test.com", firstName, "lastname"));
    }

    @Test
    void exceptionShouldThrownWhenFirstNameIsEmpty() {
        // given
        var firstName = "";
        // when + then
        assertThrows(InvalidDataException.class, () -> userService.createUser("username", "password",
                "email@test.com", firstName, "lastname"));
    }

    @Test
    void exceptionShouldThrownWhenFirstNameIsBlank() {
        // given
        var firstName = "   ";
        // when + then
        assertThrows(InvalidDataException.class, () -> userService.createUser("username", "password",
                "email@test.com", firstName, "lastname"));
    }

    @Test
    void shouldReturnUserById() {
        // given
        var uuidInString = "123e4567-e89b-12d3-a456-426614174000";
        var user = new User();
        user.setId(UUID.fromString(uuidInString));
        given(userRepository.findById(UUID.fromString(uuidInString))).willReturn(Optional.of(user));

        // when
        var userById = userService.findUserById(uuidInString);

        // then
        verify(userRepository, times(1)).findById(UUID.fromString(uuidInString));
        assertThat(userById.getId(), is(equalTo(UUID.fromString(uuidInString))));
    }

    @Test
    void shouldThrownExceptionWhenUserByIdNotFound() {
        // given
        var uuidInString = "123e4567-e89b-12d3-a456-426614174000";
        given(userRepository.findById(UUID.fromString(uuidInString))).willReturn(Optional.empty());
        // when + then
        assertThrows(NotFoundException.class, () -> userService.findUserById(uuidInString));
    }

    @Test
    void shouldThrownExceptionWhenActivateUserWithBlankKey() {
        // given
        var key = "   ";
        // when + then
        assertThrows(InvalidDataException.class, () -> userService.activateUser(key));
    }

    @Test
    void shouldEnableUserWhenFindUserByActivationKey() {
        // given
        var user = new User();
        var activationKey = "test123";
        user.setActivationKey(activationKey);
        user.setEnabled(false);
        given(userRepository.findOneByActivationKey(activationKey)).willReturn(Optional.of(user));
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(userRepository.save(any())).will(returnsFirstArg());

        // when
        userService.activateUser(activationKey);

        // then
        verify(userRepository, times(1)).save(user);
        assertThat(userRepository.findById(any()).get().getEnabled(), equalTo(true));
        assertThat(userRepository.findById(any()).get().getActivationKey(), nullValue());
    }

    @Test
    void exceptionShouldBeThrownWhenChangePasswordWithIncorrectCurrentPassword() {
        // given
        var purePassword = "password";
        var hashedPassword = "$2y$10$asOldpQaU2wLf/0VQFjO9OYJYQL4otL9OzsabsiJ7avOiIua5toVO";
        var user = new User();
        user.setId(UUID.fromString(SecurityUtils.getCurrentUser()));
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(purePassword, hashedPassword)).willReturn(false);

        // then + when
        assertThrows(InvalidDataException.class, () -> userService.changeUserPassword(SecurityUtils.getCurrentUser(),
                purePassword, null));
        verify(userRepository, times(0)).save(user);
    }

    @Test
    void exceptionShouldBeThrownWhenChangePasswordForNotCurrentUser() {

        // given
        var fakeUserId = "15d0376c-2d45-485d-bb45-92403336bf58";
        var user = new User();
        user.setId(UUID.fromString(fakeUserId));
        given(userRepository.findById(any())).willReturn(Optional.of(user));

        // when + then
        assertThrows(InvalidDataException.class, () -> userService.changeUserPassword(fakeUserId, "", ""));
        verify(userRepository, times(0)).save(user);
        verify(passwordEncoder, times(0)).matches(any(), any());
    }

    @Test
    void passwordShouldBeChangeWhenCurrentPasswordIsCorrect() {
        // given
        var user = new User();
        var oldPass = "old_pass";
        var newPass = "new_pass";
        user.setPassword(oldPass);
        user.setId(UUID.fromString(SecurityUtils.getCurrentUser()));
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(any(), any())).willReturn(true);
        given(passwordEncoder.encode(any())).willReturn(newPass);

        // when
        userService.changeUserPassword(SecurityUtils.getCurrentUser(), "pass", "pass");

        //then
        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode(any());
        assertThat(user.getPassword(), is(not(equalTo(oldPass))));
        assertThat(user.getPassword(), is(equalTo(newPass)));
    }

}
