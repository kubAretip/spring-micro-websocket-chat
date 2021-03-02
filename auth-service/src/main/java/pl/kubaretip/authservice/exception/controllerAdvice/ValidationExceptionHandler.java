package pl.kubaretip.authservice.exception.controllerAdvice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.kubaretip.exceptionutils.error.Error;
import pl.kubaretip.exceptionutils.error.Violation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ControllerAdvice
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        Set<Violation> validationErrors = new HashSet<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fieldError -> validationErrors.stream()
                        .filter(validationError -> validationError.getField().equals(fieldError.getField()))
                        .findAny()
                        .ifPresentOrElse(validationError -> {
                                    validationError.getMessage().add(fieldError.getDefaultMessage());
                                },
                                () -> {
                                    validationErrors.add(new Violation(fieldError.getField(),
                                            new ArrayList<>(Collections.singletonList(fieldError.getDefaultMessage())))
                                    );
                                }
                        )
                );

        return ResponseEntity
                .badRequest()
                .body(Error.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .violations(validationErrors)
                        .build()
                );
    }

}
