package org.digitalwallet.app.exception;

public class TransactionStatusInvalidException extends RuntimeException  {
    public TransactionStatusInvalidException(String message) {
        super(message);
    }

    public TransactionStatusInvalidException(String messageFormat, Object... args) {
        super(String.format(messageFormat, args));
    }
}