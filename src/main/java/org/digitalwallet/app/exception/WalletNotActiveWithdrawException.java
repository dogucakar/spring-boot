package org.digitalwallet.app.exception;

public class WalletNotActiveWithdrawException extends RuntimeException {
    public WalletNotActiveWithdrawException(String message) {
        super(message);
    }
}