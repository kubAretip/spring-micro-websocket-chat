package pl.kubaretip.authservice.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidAuthenticationRequestException extends AuthenticationException {
    public InvalidAuthenticationRequestException(String msg) {
        super(msg);
    }
}
