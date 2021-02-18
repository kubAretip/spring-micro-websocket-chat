package pl.kubaretip.dtomodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Setter
@Getter
@NoArgsConstructor
public class UserDTO {

    private Long id = null;

    @NotBlank
    @Size(min = 4, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String username;

    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @Size(min = 6, max = 32)
    private String password;

    @Size(max = 50)
    @NotBlank
    private String firstName;

    @Size(max = 50)
    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;


    private String friendRequestCode;

}
