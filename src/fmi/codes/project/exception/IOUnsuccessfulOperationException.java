package fmi.codes.project.exception;

public class IOUnsuccessfulOperationException extends Throwable {
    public static final String DEFAULT_MESSAGE_FORMAT = "Unsuccessful IO operation: %s";

    public IOUnsuccessfulOperationException(String message) {
        super(message);
    }

}

