package si.afridau.commerce.exception;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@Hidden // Hiding controller from swagger, idk why it wants to process it but it does
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleUnhandledRuntimeException(RuntimeException ex) {
        HttpStatus status = HttpStatus.BAD_GATEWAY;
        log.warn("Unhandled server exception: {}", ex.getLocalizedMessage());
        log.error(ex.getLocalizedMessage(), ex);
        return ResponseEntity.status(status).body(new ExceptionResponse(status, "Unhandled server exception"));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        log.warn("Authorization denied: {}", ex.getLocalizedMessage());
        log.warn(ex.getLocalizedMessage(), ex);
        return ResponseEntity.status(status).body(new ExceptionResponse(status, "Authorization denied"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(new ExceptionResponse(status, ex.getBindingResult().getFieldError().getDefaultMessage()));
    }

    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleItemNotFound(CustomRuntimeException ex) {
        log.warn("Custom exception: {}", ex.getLocalizedMessage());
        log.warn(ex.getLocalizedMessage(), ex);
        return ex.buildResponseEntity();
    }

}