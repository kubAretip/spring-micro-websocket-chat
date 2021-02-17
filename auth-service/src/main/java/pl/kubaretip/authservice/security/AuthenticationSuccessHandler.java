package pl.kubaretip.authservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import pl.kubaretip.authservice.security.model.TokenResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtils jwtUtils;

    public AuthenticationSuccessHandler(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        var out = response.getWriter();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        var token = jwtUtils.buildToken(authentication);
        var tokenJson = new ObjectMapper().writeValueAsString(new TokenResponse(token));
        out.print(tokenJson);
        out.flush();

    }
}
