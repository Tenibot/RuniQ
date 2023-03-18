package fmi.codes.project.exception;

public class InvalidCommandSyntaxException extends Throwable {
    private static final String DEFAULT_MESSAGE = "Command has invalid syntax";

    public InvalidCommandSyntaxException() {
        this(DEFAULT_MESSAGE);
    }

    public InvalidCommandSyntaxException(final String message) {
        super(message);
    }
}
