package fmi.codes.project.exception;

public class InvalidCommandSyntaxException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Command has invalid syntax";

    public InvalidCommandSyntaxException() {
        this(DEFAULT_MESSAGE);
    }

    public InvalidCommandSyntaxException(String message) {
        super(message);
    }

    public InvalidCommandSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }
}
