package si.afridau.commerce.exception.implementation;

import org.springframework.http.HttpStatus;
import si.afridau.commerce.exception.CustomRuntimeException;

public class IllegalResourceAccess extends CustomRuntimeException {
    public IllegalResourceAccess(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
