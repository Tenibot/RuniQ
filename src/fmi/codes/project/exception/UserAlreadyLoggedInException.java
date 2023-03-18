package fmi.codes.project.exception;

public class UserAlreadyLoggedInException extends Throwable {

    private static final String DEFAULT_MESSAGE = "User already logged in";

    public UserAlreadyLoggedInException() {
        this(DEFAULT_MESSAGE);
    }

    public UserAlreadyLoggedInException(final String message) {
        super(message);
    }
}
