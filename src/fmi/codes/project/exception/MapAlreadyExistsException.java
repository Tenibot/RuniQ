package fmi.codes.project.exception;

public class MapAlreadyExistsException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Map already exists";

    public MapAlreadyExistsException() {
        this(DEFAULT_MESSAGE);
    }

    public MapAlreadyExistsException(String message) {
        super(message);
    }

    public MapAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
