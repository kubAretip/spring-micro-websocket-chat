package pl.kubaretip.messageswebsocketservice.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import pl.kubaretip.authutils.jwt.JWTUtils;

@Slf4j
@Component
public class WebSocketAuthService {

    private final JWTUtils jwtUtils;

    public WebSocketAuthService(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }


    public UsernamePasswordAuthenticationToken attemptAuthentication(String authorizationHeaderValue) {

        if (jwtUtils.isValidAuthorizationHeaderValue(authorizationHeaderValue)) {
            try {
                var token = authorizationHeaderValue.replace(jwtUtils.getJwtConfig().getTokenPrefix(), "");
                return jwtUtils.getAuthentication(token);
            } catch (JWTDecodeException | TokenExpiredException ex) {
                log.error("Invalid token");
            }
        }
        return null;
    }


}
