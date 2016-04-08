package de.craften.craftenlauncher.exception;

public class CraftenAuthenticationException extends CraftenLogicException {
    private final Reason reason;

    public CraftenAuthenticationException(Reason reason) {
        super(reason.name());
        this.reason = reason;
    }

    public CraftenAuthenticationException(Reason reason, Throwable cause) {
        super(reason.name(), cause);
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }

    public enum Reason {
        DID_NOT_BUY_MINECRAFT,
        USER_CREDENTIALS_ARE_WRONG
    }
}

