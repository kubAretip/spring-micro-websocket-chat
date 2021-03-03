package pl.kubaretip.exceptionutils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.kubaretip.exceptionutils.error.Error;

import javax.servlet.http.HttpServletRequest;

public interface CommonExceptionHandler {

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    default Error handleNotFound(final NotFoundException ex, HttpServletRequest request) {
        return Error.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .detail(ex.getMessage())
                .path(request.getRequestURI())
                .title(HttpStatus.NOT_FOUND.getReasonPhrase())
                .build();
    }

    @ExceptionHandler(value = {AlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    default Error handleAlreadyExists(final AlreadyExistsException ex, HttpServletRequest request) {
        return Error.builder()
                .status(HttpStatus.CONFLICT.value())
                .detail(ex.getMessage())
                .path(request.getRequestURI())
                .title(HttpStatus.CONFLICT.getReasonPhrase())
                .build();
    }

    @ExceptionHandler(value = {InvalidDataException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    default Error handleInvalidData(final InvalidDataException ex, HttpServletRequest request) {
        return Error.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .detail(ex.getMessage())
                .path(request.getRequestURI())
                .title(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .build();
    }

}
