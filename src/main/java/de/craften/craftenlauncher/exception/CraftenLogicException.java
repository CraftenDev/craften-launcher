package de.craften.craftenlauncher.exception;

public class CraftenLogicException extends CraftenException {
    public CraftenLogicException(String message) {
        super(message);
    }

    public CraftenLogicException(Throwable cause) {
        super(cause);
    }

    public CraftenLogicException(String message, Throwable cause) {
        super(message, cause);
    }
}
