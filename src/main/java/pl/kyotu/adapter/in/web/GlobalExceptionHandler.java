package pl.kyotu.adapter.in.web;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.kyotu.domain.exception.ElevatorNotFoundException;
import pl.kyotu.domain.exception.InvalidFloorException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidFloorException.class)
    public ResponseEntity<Map<String, String>> handleFloor(InvalidFloorException ex) {
        return body(HttpStatus.BAD_REQUEST, "invalid_floor", ex.getMessage());
    }

    @ExceptionHandler(ElevatorNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleMissing(ElevatorNotFoundException ex) {
        return body(HttpStatus.NOT_FOUND, "elevator_not_found", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBad(IllegalArgumentException ex) {
        return body(HttpStatus.BAD_REQUEST, "bad_request", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        return body(HttpStatus.BAD_REQUEST, "validation_error",
                ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage());
    }

    private ResponseEntity<Map<String, String>> body(HttpStatus s, String err, String msg) {
        return ResponseEntity.status(s).body(Map.of("error", err, "message", msg));
    }
}
