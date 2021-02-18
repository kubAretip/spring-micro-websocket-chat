package pl.kubaretip.userservice.exception;

import org.springframework.http.HttpStatus;

public class ClientException extends RuntimeException {

    public HttpStatus statusCode;

    public ClientException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }


}
