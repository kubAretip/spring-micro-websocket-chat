package pl.kubaretip.authservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pl.kubaretip.authservice.security.model.TokenResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTBuilder jwtBuilder;
    private final ObjectMapper objectMapper;

    public AuthenticationSuccessHandler(JWTBuilder jwtBuilder,
                                        ObjectMapper objectMapper) {
        this.jwtBuilder = jwtBuilder;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        var out = response.getWriter();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        var token = jwtBuilder.buildToken(authentication);
        var tokenJson = objectMapper.writeValueAsString(new TokenResponse(token));
        out.print(tokenJson);
        out.flush();
    }
}
