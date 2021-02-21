package pl.kubaretip.chatservice.exception.handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import pl.kubaretip.chatservice.exception.AlreadyExistsException;
import pl.kubaretip.chatservice.exception.InvalidDataException;
import pl.kubaretip.chatservice.exception.NotFoundException;

import java.net.URI;

@RestControllerAdvice
@Slf4j
public class CustomExceptionsHandler {

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Problem handleNotFound(final NotFoundException ex, WebRequest request) {
        return Problem.builder()
                .withStatus(Status.NOT_FOUND)
                .withDetail(ex.getLocalizedMessage())
                .withTitle(Status.NOT_FOUND.getReasonPhrase())
                .withType(URI.create(request.getContextPath()))
                .build();
    }

    @ExceptionHandler(value = {AlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public Problem handleAlreadyExists(final AlreadyExistsException ex, WebRequest request) {
        return Problem.builder()
                .withStatus(Status.CONFLICT)
                .withDetail(ex.getLocalizedMessage())
                .withTitle(Status.CONFLICT.getReasonPhrase())
                .withType(URI.create(request.getContextPath()))
                .build();
    }

    @ExceptionHandler(value = {InvalidDataException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Problem handleInvalidData(final InvalidDataException ex, WebRequest request) {
        return Problem.builder()
                .withStatus(Status.BAD_REQUEST)
                .withDetail(ex.getLocalizedMessage())
                .withTitle(Status.BAD_REQUEST.getReasonPhrase())
                .withType(URI.create(request.getContextPath()))
                .build();
    }

}
