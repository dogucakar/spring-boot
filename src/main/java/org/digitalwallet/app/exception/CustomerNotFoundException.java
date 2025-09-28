package org.digitalwallet.app.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String message) {
        super(message);
    }

    public CustomerNotFoundException(String messageFormat, Object... args) {
        super(String.format(messageFormat, args));
    }
}
