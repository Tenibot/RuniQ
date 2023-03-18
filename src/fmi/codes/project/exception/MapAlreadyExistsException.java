package fmi.codes.project.exception;

public class MapAlreadyExistsException extends Throwable {
    private static final String DEFAULT_MESSAGE = "Map already exists";

    public MapAlreadyExistsException() {
        this(DEFAULT_MESSAGE);
    }

    public MapAlreadyExistsException(final String message) {
        super(message);
    }
}
