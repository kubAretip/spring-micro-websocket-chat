package pl.kubaretip.authservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.kubaretip.authservice.exception.InvalidAuthenticationRequestException;
import pl.kubaretip.authservice.security.model.AuthRequestModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        AuthRequestModel authenticationRequest;
        try {
            authenticationRequest = new ObjectMapper().readValue(request.getInputStream(), AuthRequestModel.class);
        } catch (IOException e) {
            throw new InvalidAuthenticationRequestException("Invalid login request");
        }
        log.debug("Attempt authentication user: " + authenticationRequest.getUsername());
        var authentication = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        return this.getAuthenticationManager().authenticate(authentication);
    }
}
