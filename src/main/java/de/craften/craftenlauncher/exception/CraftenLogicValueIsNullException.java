package de.craften.craftenlauncher.exception;

public class CraftenLogicValueIsNullException extends CraftenLogicException {

    public CraftenLogicValueIsNullException(String message) {
        super(message);
    }

    public CraftenLogicValueIsNullException(Throwable cause) {
        super(cause);
    }

    public CraftenLogicValueIsNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public CraftenLogicValueIsNullException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
