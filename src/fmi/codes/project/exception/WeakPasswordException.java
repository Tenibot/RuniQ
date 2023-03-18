package fmi.codes.project.exception;

public class WeakPasswordException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Password is weak";

    public WeakPasswordException() {
        this(DEFAULT_MESSAGE);
    }

    public WeakPasswordException(String message) {
        super(message);
    }

    public WeakPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

}
