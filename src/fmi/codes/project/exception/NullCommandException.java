package fmi.codes.project.exception;

public class NullCommandException extends Throwable {

    private static final String DEFAULT_MESSAGE = "Command is null";

    public NullCommandException() {
        this(DEFAULT_MESSAGE);
    }

    public NullCommandException(final String message) {
        super(message);
    }
}
