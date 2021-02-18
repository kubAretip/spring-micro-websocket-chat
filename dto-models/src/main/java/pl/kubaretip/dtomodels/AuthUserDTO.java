package pl.kubaretip.dtomodels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class AuthUserDTO {

    private Long id;
    private String username;
    private String password;
    private String activationKey;

}
