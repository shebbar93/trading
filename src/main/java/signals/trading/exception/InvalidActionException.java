package signals.trading.exception;

public class InvalidActionException extends RuntimeException {
    public InvalidActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
