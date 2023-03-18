package fmi.codes.project.exception;

public class NoSuchUserException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "No such user";

    public NoSuchUserException() {
        this(DEFAULT_MESSAGE);
    }

    public NoSuchUserException(String message) {
        super(message);
    }
    public NoSuchUserException(String message, Throwable cause) {
        super(message, cause);
    }

}
