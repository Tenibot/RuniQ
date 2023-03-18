package fmi.codes.project.exception;

public class IOUnsuccessfulOperationException extends RuntimeException {
    public static final String DEFAULT_MESSAGE_FORMAT = "Unsuccessful IO operation: %s";

    public IOUnsuccessfulOperationException() {
        this(DEFAULT_MESSAGE_FORMAT);
    }

    public IOUnsuccessfulOperationException(String message) {
        super(message);
    }

    public IOUnsuccessfulOperationException(String message, Throwable cause) {
        super(message, cause);
    }

}

