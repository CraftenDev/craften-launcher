package de.craften.craftenlauncher.exception;

public class CraftenLogicException extends CraftenException {
    public CraftenLogicException() {
    }

    public CraftenLogicException(String message) {
        super(message);
    }

    public CraftenLogicException(Throwable cause) {
        super(cause);
    }

    public CraftenLogicException(String message, Throwable cause) {
        super(message, cause);
    }

    public CraftenLogicException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
