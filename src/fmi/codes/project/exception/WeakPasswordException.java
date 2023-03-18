package fmi.codes.project.exception;

public class WeakPasswordException extends Throwable {
    private static final String DEFAULT_MESSAGE = "Password is weak";

    public WeakPasswordException() {
        this(DEFAULT_MESSAGE);
    }

    public WeakPasswordException(final String message) {
        super(message);
    }

}
