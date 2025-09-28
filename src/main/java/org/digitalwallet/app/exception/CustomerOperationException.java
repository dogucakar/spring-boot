package org.digitalwallet.app.exception;

public class CustomerOperationException extends RuntimeException {
    public CustomerOperationException(String message) {
        super(message);
    }

    public CustomerOperationException(String messageFormat, Object... args) {
        super(String.format(messageFormat, args));
    }
}