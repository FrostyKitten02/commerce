package si.afridau.commerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class CustomRuntimeException extends RuntimeException {
    private final HttpStatus httpStatus;

    public CustomRuntimeException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public CustomRuntimeException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ExceptionResponse buildExceptionResponse() {
        return new ExceptionResponse(this.httpStatus, this.getMessage());
    }

    public ResponseEntity<ExceptionResponse> buildResponseEntity() {
        return ResponseEntity.status(this.httpStatus).body(this.buildExceptionResponse());
    }

}