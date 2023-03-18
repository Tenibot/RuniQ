package fmi.codes.project.exception;

public class InaccessibleCommandException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Inaccessible command, you must first join a game session";

    public InaccessibleCommandException() {
        this(DEFAULT_MESSAGE);
    }

    public InaccessibleCommandException(String message) {
        super(message);
    }

    public InaccessibleCommandException(String message, Throwable cause) {
        super(message, cause);
    }

}
