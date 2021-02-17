package pl.kubaretip.zuulserver.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.kubaretip.authutils.JWTUtils;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;

    public JWTAuthorizationFilter(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

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
