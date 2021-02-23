package pl.kubaretip.authservice.web.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.kubaretip.authservice.constants.ApplicationConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class ChangePassRequest {

    @NotBlank
    @Size(min = ApplicationConstants.USER_PASSWORD_MIN_LENGTH, max = ApplicationConstants.USER_PASSWORD_MIN_LENGTH)
    private String currentPassword;
    private String newPassword;

}
