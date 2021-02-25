package pl.kubaretip.authservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    public AuthenticationFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        var out = response.getWriter();

        Problem problem = Problem.builder()
                .withStatus(Status.UNAUTHORIZED)
                .withDetail(exception.getLocalizedMessage())
                .withTitle(Status.UNAUTHORIZED.getReasonPhrase())
                .build();

        var jsonErrorResponse = objectMapper.writeValueAsString(problem);
        out.print(jsonErrorResponse);
        out.flush();
    }


}
