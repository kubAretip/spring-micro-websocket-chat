package pl.kubaretip.authservice.rest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.util.UriComponentsBuilder;
import pl.kubaretip.authservice.mapper.UserMapper;
import pl.kubaretip.authservice.service.UserActivationMessagingService;
import pl.kubaretip.authservice.service.UserService;
import pl.kubaretip.dtomodels.UserDTO;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserActivationMessagingService userActivationMessagingService;

    public UserController(UserService userService,
                          UserMapper userMapper,
                          UserActivationMessagingService userActivationMessagingService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.userActivationMessagingService = userActivationMessagingService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createNewAuthUser(@Valid @RequestBody UserDTO userDTO,
                                                     UriComponentsBuilder uriComponentsBuilder) {
        var user = userService.createUser(userDTO.getUsername(), userDTO.getPassword(),
                userDTO.getEmail(), userDTO.getFirstName(), userDTO.getLastName());
        userActivationMessagingService.sendActivationMail(userMapper.mapToUserDTO(user));
        var userDTOResponse = userMapper.mapToUserDTOWithoutActivationKey(user);
        var location = uriComponentsBuilder.path("/users/{id}")
                .buildAndExpand(userDTOResponse.getId()).toUri();
        return ResponseEntity.created(location).body(userDTOResponse);
    }


    @PatchMapping("/activate")
    public ResponseEntity<Void> activateAccount(@RequestParam("data") String activationKey) {
        userService.activateUser(activationKey);
        return ResponseEntity.noContent().build();
    }


}
