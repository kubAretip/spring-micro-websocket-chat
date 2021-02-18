package pl.kubaretip.userservice.exception.controllerAdvice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.context.request.WebRequest;
import pl.kubaretip.userservice.exception.ClientException;

@ControllerAdvice
public class ClientExceptionHandler {

    @ExceptionHandler(value = {ClientException.class})
    public ResponseEntity<String> handleClient(ClientException ex, WebRequest webRequest) {
        return new ResponseEntity<>(ex.getLocalizedMessage(), ex.getStatusCode());
    }


}
