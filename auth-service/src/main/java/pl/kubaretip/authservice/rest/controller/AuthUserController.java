package pl.kubaretip.authservice.rest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.util.UriComponentsBuilder;
import pl.kubaretip.authservice.mapper.AuthUserMapper;
import pl.kubaretip.authservice.service.AuthUserService;
import pl.kubaretip.dtomodels.AuthUserDTO;

@Slf4j
@RestController
@RequestMapping("/auth-users")
public class AuthUserController {

    private final AuthUserService authUserService;
    private final AuthUserMapper authUserMapper;

    public AuthUserController(AuthUserService authUserService,
                              AuthUserMapper authUserMapper) {
        this.authUserService = authUserService;
        this.authUserMapper = authUserMapper;
    }

    @PostMapping
    public ResponseEntity<AuthUserDTO> createNewAuthUser(@RequestBody AuthUserDTO authUserDTO,
                                                         UriComponentsBuilder uriComponentsBuilder) {
        var authUser = authUserService.createAuthUser(authUserDTO.getUsername(), authUserDTO.getPassword());
        var location = uriComponentsBuilder.path("/auth-users/{id}")
                .buildAndExpand(authUser.getId()).toUri();
        return ResponseEntity.created(location).body(authUserMapper.mapToAuthUserDTO(authUser));
    }


}
