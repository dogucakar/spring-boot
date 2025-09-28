package org.digitalwallet.app.exception;

public class TransactionNotFoundException extends RuntimeException  {
    public TransactionNotFoundException(String message) {
        super(message);
    }

    public TransactionNotFoundException(String messageFormat, Object... args) {
        super(String.format(messageFormat, args));
    }
}