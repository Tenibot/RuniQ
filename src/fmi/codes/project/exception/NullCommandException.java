package fmi.codes.project.exception;

public class NullCommandException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Command is null";

    public NullCommandException() {
        this(DEFAULT_MESSAGE);
    }

    public NullCommandException(String message) {
        super(message);
    }

    public NullCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
