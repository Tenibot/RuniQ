package fmi.codes.project.exception;

public class UsernameAlreadyUsedException extends Throwable {

    private static final String DEFAULT_MESSAGE = "Username already used";

    public UsernameAlreadyUsedException() {
        this(DEFAULT_MESSAGE);
    }

    public UsernameAlreadyUsedException(final String message) {
        super(message);
    }
}
