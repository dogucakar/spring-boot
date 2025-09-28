package org.digitalwallet.app.service;

import lombok.AllArgsConstructor;
import org.digitalwallet.app.enumeration.TransactionStatus;
import org.digitalwallet.app.enumeration.TransactionType;
import org.digitalwallet.app.exception.TransactionNotFoundException;
import org.digitalwallet.app.repository.TransactionRepository;
import org.digitalwallet.app.repository.model.Transaction;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }
    public Transaction findById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Wallet not found with id: ", transactionId));
    }

    public void createTransaction(Long walletId, Double amount, String oppositeParty, TransactionType transactionType, boolean isPending) {
        Transaction transaction = new Transaction();
        transaction.setWalletId(walletId);
        transaction.setAmount(amount);
        transaction.setOppositeParty(oppositeParty);
        transaction.setType(transactionType);
        transaction.setStatus(isPending ? TransactionStatus.PENDING : TransactionStatus.APPROVED);
        transactionRepository.save(transaction);
    }

    public void listTransactions(Long walletId) {
        transactionRepository.findByWalletId(walletId);
    }
}