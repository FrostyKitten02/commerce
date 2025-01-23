package si.afridau.commerce.catalog.exceptions.implementation;

import org.springframework.http.HttpStatus;
import si.afridau.commerce.catalog.exceptions.CustomRuntimeException;

public class InternalServerException extends CustomRuntimeException {
    private static final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final String defaultMsg = "Internal server error";

    public InternalServerException() {
        super(defaultMsg, status);
    }
    public InternalServerException(String message) {
        super(message, status);
    }
    public InternalServerException(String message, Throwable throwable) {
        super(message, throwable, status);
    }
}
