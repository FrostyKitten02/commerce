package si.afridau.commerce.catalog.exceptions.implementation;

import org.springframework.http.HttpStatus;
import si.afridau.commerce.catalog.exceptions.CustomRuntimeException;

public class IllegalResourceAccess extends CustomRuntimeException {
    public IllegalResourceAccess(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
