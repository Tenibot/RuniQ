package fmi.codes.project.exception;

public class InvalidUsernameException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Username is invalid";

    public InvalidUsernameException() {
        this(DEFAULT_MESSAGE);
    }

    public InvalidUsernameException(String message) {
        super(message);
    }

    public InvalidUsernameException(String message, Throwable cause) {
        super(message, cause);
    }


}
