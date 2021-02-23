package pl.kubaretip.authservice.web.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pl.kubaretip.authservice.mapper.UserMapper;
import pl.kubaretip.authservice.messaging.sender.UserSender;
import pl.kubaretip.authservice.service.UserService;
import pl.kubaretip.dtomodels.UserDTO;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserSender userSender;

    public UserController(UserService userService,
                          UserMapper userMapper,
                          UserSender userSender) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.userSender = userSender;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createNewUser(@Valid @RequestBody UserDTO userDTO,
                                                 UriComponentsBuilder uriComponentsBuilder) {
        var user = userService.createUser(userDTO.getUsername(), userDTO.getPassword(),
                userDTO.getEmail(), userDTO.getFirstName(), userDTO.getLastName());

        userSender.send(userMapper.mapToUserDTO(user));

        var location = uriComponentsBuilder.path("/users/{id}")
                .buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(location).body(userMapper.mapToUserDTOWithoutActivationKey(user));
    }


    @PatchMapping("/activate")
    public ResponseEntity<Void> activateAccount(@RequestParam("data") String activationKey) {
        userService.activateUser(activationKey);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") String userId) {
        return ResponseEntity.ok(userMapper.mapToUserDTO(userService.findUserById(userId)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> editUser(@PathVariable("id") String userId, @RequestBody UserDTO userDTO) {
        var user = userService.modifyUser(userId, userDTO.getFirstName(), userDTO.getLastName());
        return ResponseEntity.ok(userMapper.mapToUserDTO(user));
    }


}
