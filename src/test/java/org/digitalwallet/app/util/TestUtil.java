package org.digitalwallet.app.util;

import org.digitalwallet.app.dto.WalletDTO;
import org.digitalwallet.app.dto.request.DepositRequest;
import org.digitalwallet.app.dto.request.TransactionOperationRequest;
import org.digitalwallet.app.dto.request.WalletRequest;
import org.digitalwallet.app.dto.request.WithdrawRequest;
import org.digitalwallet.app.enumeration.*;
import org.digitalwallet.app.repository.model.Transaction;
import org.digitalwallet.app.repository.model.UserAuth;
import org.digitalwallet.app.repository.model.Wallet;

public class TestUtil {
    public static WalletRequest createWalletRequest () {
        WalletRequest walletRequest = new WalletRequest();
        walletRequest.setCustomerId(1L);
        walletRequest.setWalletName("TEST");
        walletRequest.setCurrency(Currency.EUR.toString());
        walletRequest.setActiveForShopping(true);
        walletRequest.setActiveForWithdraw(true);
        return walletRequest;
    }

    public static DepositRequest createDepositRequest(Double amount) {
        DepositRequest request = new DepositRequest();
        request.setSource("TR12345");
        request.setAmount(amount);
        return request;
    }

    public static WithdrawRequest createWithdrawRequest(Double amount) {
        WithdrawRequest request = new WithdrawRequest();
        request.setDestination("TR12345");
        request.setAmount(amount);
        return request;
    }

    public static Wallet createWallet(Long walletId) {
        Wallet wallet = new Wallet();
        wallet.setWalletName("TEST");
        wallet.setCurrency(Currency.EUR.toString());
        wallet.setCustomerId(1L);
        wallet.setId(walletId);
        wallet.setBalance(3000d);
        wallet.setUsableBalance(3000d);
        wallet.setActiveForShopping(true);
        wallet.setActiveForWithdraw(true);
        return wallet;
    }

    public static WalletDTO createWalletDTO(Long walletId) {
        WalletDTO walletDTO = new WalletDTO();
        walletDTO.setWalletName("TEST");
        walletDTO.setCurrency(Currency.EUR.toString());
        walletDTO.setActiveForShopping(true);
        walletDTO.setActiveForWithdraw(true);
        return walletDTO;
    }

    public static Transaction createTransaction(TransactionType transactionType, TransactionStatus transactionStatus) {
        Transaction transaction = new Transaction();
        transaction.setOppositeParty("TR123");
        transaction.setType(transactionType);
        transaction.setWalletId(1L);
        transaction.setStatus(transactionStatus);
        transaction.setAmount(1000d);
        transaction.setOppositePartyType(OppositePartyType.IBAN);
        return transaction;
    }

    public static TransactionOperationRequest createTransactionRequest(TransactionStatus transactionStatus) {
        TransactionOperationRequest transactionOperationRequest = new TransactionOperationRequest();
        transactionOperationRequest.setTransactionId(1L);
        transactionOperationRequest.setStatus(transactionStatus);
        return transactionOperationRequest;
    }

    public static UserAuth createUser() {
        UserAuth userAuth = new UserAuth();
        userAuth.setRole(Role.CUSTOMER);
        userAuth.setCustomerId(1L);
        userAuth.setUsername("user");
        userAuth.setPassword("pass");
        return userAuth;
    }
}
