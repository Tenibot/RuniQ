package fmi.codes.project.exception;

public class NoSuchUserException extends Throwable {
    private static final String DEFAULT_MESSAGE = "No such user";

    public NoSuchUserException() {
        this(DEFAULT_MESSAGE);
    }

    public NoSuchUserException(final String message) {
        super(message);
    }

}
