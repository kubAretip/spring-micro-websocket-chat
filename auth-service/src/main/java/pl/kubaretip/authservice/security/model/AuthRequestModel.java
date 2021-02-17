package pl.kubaretip.authservice.security.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthRequestModel {

    private String username;
    private String password;

}
