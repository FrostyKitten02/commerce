package si.afridau.commerce.catalog.exceptions.implementation;

import org.springframework.http.HttpStatus;
import si.afridau.commerce.catalog.exceptions.CustomRuntimeException;

public class ItemNotFoundException extends CustomRuntimeException {
    public ItemNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}