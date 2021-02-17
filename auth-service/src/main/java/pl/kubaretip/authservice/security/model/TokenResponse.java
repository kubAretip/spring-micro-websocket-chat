package pl.kubaretip.authservice.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {

    @JsonProperty("access_token")
    private String accessToken;


}
