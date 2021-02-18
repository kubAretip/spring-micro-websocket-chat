package pl.kubaretip.authservice.exception.controllerAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import pl.kubaretip.authservice.exception.UserAlreadyExistsException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Problem handlerUserAlreadyExists(final UserAlreadyExistsException ex, WebRequest request) {
        return Problem.builder()
                .withStatus(Status.CONFLICT)
                .withDetail(ex.getLocalizedMessage())
                .withTitle(Status.CONFLICT.getReasonPhrase())
                .build();
    }

}