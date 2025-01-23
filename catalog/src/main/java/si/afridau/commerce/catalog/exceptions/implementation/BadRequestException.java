package si.afridau.commerce.catalog.exceptions.implementation;

import org.springframework.http.HttpStatus;
import si.afridau.commerce.catalog.exceptions.CustomRuntimeException;


public class BadRequestException extends CustomRuntimeException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
