package fmi.codes.project.exception;

public class WrongPasswordException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Password does not match";

    public WrongPasswordException() {
        this(DEFAULT_MESSAGE);
    }

    public WrongPasswordException(String message) {
        super(message);
    }

    public WrongPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

}