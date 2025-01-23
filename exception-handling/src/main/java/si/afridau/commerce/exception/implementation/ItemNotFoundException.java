package si.afridau.commerce.exception.implementation;

import org.springframework.http.HttpStatus;
import si.afridau.commerce.exception.CustomRuntimeException;

public class ItemNotFoundException extends CustomRuntimeException {
    public ItemNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}