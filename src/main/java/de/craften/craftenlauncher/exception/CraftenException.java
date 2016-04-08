package de.craften.craftenlauncher.exception;

public class CraftenException extends Exception {
    public CraftenException() {
    }

    public CraftenException(String message) {
        super(message);
    }

    public CraftenException(Throwable cause) {
        super(cause);
    }

    public CraftenException(String message, Throwable cause) {
        super(message, cause);
    }

    public CraftenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause);
    }
}
