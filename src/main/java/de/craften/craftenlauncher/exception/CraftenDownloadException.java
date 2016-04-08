package de.craften.craftenlauncher.exception;

public class CraftenDownloadException extends CraftenLogicException {
    public CraftenDownloadException(String message) {
        super(message);
    }

    public CraftenDownloadException(String message, Throwable cause) {
        super(message, cause);
    }
}
