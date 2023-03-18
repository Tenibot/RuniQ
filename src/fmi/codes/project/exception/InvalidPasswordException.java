package fmi.codes.project.exception;

public class InvalidPasswordException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Password is incorrect";

    public InvalidPasswordException() {
        this(DEFAULT_MESSAGE);
    }

    public InvalidPasswordException(String message) {
        super(message);
    }

    public InvalidPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
