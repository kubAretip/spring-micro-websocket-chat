package pl.kubaretip.authservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import pl.kubaretip.authservice.utils.Error;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        var out = response.getWriter();

        var errorBuilder = Error.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .title("Unauthorized")
                .detail(exception.getLocalizedMessage());

        var jsonErrorResponse = new ObjectMapper().writeValueAsString(errorBuilder.build());
        out.print(jsonErrorResponse);
        out.flush();
    }


}
