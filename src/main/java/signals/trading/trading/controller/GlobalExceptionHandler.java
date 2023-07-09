package signals.trading.trading.controller;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import signals.trading.trading.exception.InvalidSignalTypeException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidSignalTypeException.class)
    public ResponseEntity<String> handleInvalidSignalType(InvalidSignalTypeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(TypeMismatchException ex) {
        return ResponseEntity.badRequest().body("Invalid signal type. Please provide an integer value.");
    }
}
