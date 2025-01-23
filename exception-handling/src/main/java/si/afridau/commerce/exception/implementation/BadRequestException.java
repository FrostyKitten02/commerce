package si.afridau.commerce.exception.implementation;

import org.springframework.http.HttpStatus;
import si.afridau.commerce.exception.CustomRuntimeException;

public class BadRequestException extends CustomRuntimeException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
