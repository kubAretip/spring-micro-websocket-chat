package pl.kubaretip.authservice.exception;

import org.springframework.security.core.AuthenticationException;

public class UserNotActivatedException extends AuthenticationException {
    public UserNotActivatedException(String msg) {
        super(msg);
    }
}
