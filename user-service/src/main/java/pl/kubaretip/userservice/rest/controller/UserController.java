package pl.kubaretip.userservice.rest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import pl.kubaretip.dtomodels.UserDTO;
import pl.kubaretip.userservice.mapper.UserMapper;
import pl.kubaretip.userservice.service.UserService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService,
                          UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO userDTO,
                                                UriComponentsBuilder uriComponentsBuilder) {
        var user = userService.createUser(userDTO);
        var location = uriComponentsBuilder.path("/users/{id}")
                .buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(location).body(userMapper.mapToUserDTO(user));
    }


}
