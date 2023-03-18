package fmi.codes.project.exception;

public class InvalidServerResponseException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Server response is invalid";

    public InvalidServerResponseException() {
        this(DEFAULT_MESSAGE);
    }

    public InvalidServerResponseException(String message) {
        super(message);
    }

    public InvalidServerResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
