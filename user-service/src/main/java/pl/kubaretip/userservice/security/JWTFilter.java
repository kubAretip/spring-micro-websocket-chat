package pl.kubaretip.userservice.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pl.kubaretip.authutils.JWTUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class JWTFilter extends BasicAuthenticationFilter {

    private final JWTUtils jwtUtils;

    public JWTFilter(AuthenticationManager authenticationManager, JWTUtils jwtUtils) {
        super(authenticationManager);
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        var authorizationHeaderValue = request.getHeader(jwtUtils.getJwtConfig().getHeader());
        if (jwtUtils.isValidAuthorizationHeaderValue(authorizationHeaderValue)) {
            var token = authorizationHeaderValue.replace(jwtUtils.getJwtConfig().getTokenPrefix(), "");
            try {
                var authentication = jwtUtils.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JWTDecodeException | TokenExpiredException ex) {
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }
}
