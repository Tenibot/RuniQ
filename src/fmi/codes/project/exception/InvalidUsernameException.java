package fmi.codes.project.exception;

public class InvalidUsernameException extends Throwable {

    private static final String DEFAULT_MESSAGE = "Username is invalid";

    public InvalidUsernameException() {
        this(DEFAULT_MESSAGE);
    }

    public InvalidUsernameException(final String message) {
        super(message);
    }
}
