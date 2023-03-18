package fmi.codes.project.exception;

public class UsernameAlreadyUsedException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Username already used";

    public UsernameAlreadyUsedException() {
        this(DEFAULT_MESSAGE);
    }

    public UsernameAlreadyUsedException(String message) {
        super(message);
    }

    public UsernameAlreadyUsedException(String message, Throwable cause) {
        super(message, cause);
    }
}
