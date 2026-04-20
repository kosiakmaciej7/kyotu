package pl.kyotu.domain.exception;

public class ElevatorNotFoundException extends RuntimeException {
    public ElevatorNotFoundException(String message) { super(message); }
}
