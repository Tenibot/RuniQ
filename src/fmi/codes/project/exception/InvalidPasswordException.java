package fmi.codes.project.exception;

public class InvalidPasswordException extends Throwable {
    private static final String DEFAULT_MESSAGE = "Password is incorrect";

    public InvalidPasswordException() {
        this(DEFAULT_MESSAGE);
    }

    public InvalidPasswordException(final String message) {
        super(message);
    }
}
