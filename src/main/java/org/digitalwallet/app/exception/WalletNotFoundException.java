package org.digitalwallet.app.exception;

public class WalletNotFoundException extends RuntimeException  {
    public WalletNotFoundException(String message) {
        super(message);
    }

    public WalletNotFoundException(String messageFormat, Object... args) {
        super(String.format(messageFormat, args));
    }
}
