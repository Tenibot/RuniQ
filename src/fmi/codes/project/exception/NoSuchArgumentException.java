package fmi.codes.project.exception;

public class NoSuchArgumentException extends RuntimeException {
    public static final String DEFAULT_MESSAGE_FORMAT = "No such argument found: %s";

    public NoSuchArgumentException() {
        this(DEFAULT_MESSAGE_FORMAT);
    }

    public NoSuchArgumentException(String message) {
        super(message);
    }

    public NoSuchArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
