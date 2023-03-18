package fmi.codes.project.exception;

public class InaccessibleCommandException extends Throwable {
    private static final String DEFAULT_MESSAGE = "Inaccessible command, you must first join a game session";

    public InaccessibleCommandException() {
        this(DEFAULT_MESSAGE);
    }

    public InaccessibleCommandException(final String message) {
        super(message);
    }

}
