package pl.kubaretip.authservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class JWTConfig {

    @Value("${security.jwt.uri:/authenticate}")
    private String authEndpoint;

    @Value("${security.jwt.header:Authorization}")
    private String header;

    @Value("${security.jwt.prefix:Bearer }")
    private String tokenPrefix;

    @Value("${security.jwt.expiration:#{24*60*60}}")
    private int expiration;

    @Value("${security.jwt.secret:jwtSecretKey123}")
    private String secret;

}
