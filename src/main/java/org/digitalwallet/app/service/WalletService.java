package org.digitalwallet.app.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.digitalwallet.app.dto.WalletDTO;
import org.digitalwallet.app.dto.request.*;
import org.digitalwallet.app.enumeration.TransactionStatus;
import org.digitalwallet.app.enumeration.TransactionType;
import org.digitalwallet.app.exception.CustomerOperationException;
import org.digitalwallet.app.exception.WalletNotActiveWithdrawException;
import org.digitalwallet.app.exception.WalletNotFoundException;
import org.digitalwallet.app.mapper.WalletMapper;
import org.digitalwallet.app.repository.WalletRepository;
import org.digitalwallet.app.repository.model.Wallet;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    private final TransactionService transactionService;
    private final UserAuthService userAuthService;

    public Wallet findById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found with id: ", walletId));
    }

    public void createWallet(WalletRequest walletRequest, Authentication auth) {
        if (customerCheck(auth, walletRequest.getCustomerId())) {
            walletRepository.save(walletMapper.toEntity(walletRequest));
        }
    }

    public List<WalletDTO> listWallets(Long customerId, WalletFilterRequest filter, Authentication auth) {
        if (customerCheck(auth, customerId)) {
            var wallets = walletRepository.findByCustomerIdAndCurrencyAndWalletName(customerId, filter.getCurrency(), filter.getWalletName());
            return walletMapper.toDtoList(wallets);
        }
        return Collections.EMPTY_LIST;
    }

    @Transactional
    public void deposit(Long walletId, DepositRequest request, Authentication auth) {
        var wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found with id: ", walletId));
        if (customerCheck(auth, wallet.getCustomerId())) {
            double amount = request.getAmount();
            boolean isPending = amount > 1000;
            updateWallet(wallet, amount, TransactionType.DEPOSIT, isPending);
            transactionService.createTransaction(wallet.getId(), amount, request.getSource(), TransactionType.DEPOSIT, isPending);
        }
    }

    @Transactional
    public void withdraw(Long walletId, WithdrawRequest request, Authentication auth) {
        var wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found with id: ", walletId));
        if (customerCheck(auth, wallet.getCustomerId())) {
            if (!wallet.isActiveForWithdraw()) {
                throw new WalletNotActiveWithdrawException("Wallet is not active for withdraw");
            }
            double amount = request.getAmount();
            boolean isPending = amount > 1000;
            updateWallet(wallet, amount, TransactionType.WITHDRAW, isPending);
            transactionService.createTransaction(wallet.getId(), amount, request.getDestination(), TransactionType.WITHDRAW, isPending);
        }
    }

    public void updateWallet(Wallet wallet, double amount, TransactionType transactionType, boolean isPending) {
        if (TransactionType.DEPOSIT.equals(transactionType)) {
            if (isPending) {
                wallet.setBalance(wallet.getBalance() + amount);
            } else {
                double updatedBalance = wallet.getUsableBalance() + amount;
                wallet.setBalance(updatedBalance);
                wallet.setUsableBalance(updatedBalance);
            }
        } else if (TransactionType.WITHDRAW.equals(transactionType)) {
            if (isPending) {
                wallet.setUsableBalance(wallet.getBalance() - amount);
            } else {
                double updatedBalance = wallet.getUsableBalance() - amount;
                wallet.setBalance(updatedBalance);
                wallet.setUsableBalance(updatedBalance);
            }
        }
        walletRepository.save(wallet);
    }

    @Transactional
    public void progressTransaction(TransactionOperationRequest transactionOperationRequest, Authentication auth) {
        var transaction = transactionService.findById(transactionOperationRequest.getTransactionId());
        Long walletId = transaction.getWalletId();
        var wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found with id: ", walletId));
        if (customerCheck(auth, wallet.getCustomerId())) {
            transaction.setStatus(transactionOperationRequest.getStatus());
            transactionService.save(transaction);
            setBalances(transaction.getWalletId(), transaction.getAmount(), transaction.getType(), transactionOperationRequest.getStatus());
        }
    }

    private void setBalances(Long walletId, Double amount, TransactionType transactionType, TransactionStatus newStatus) {
        var wallet = findById(walletId);
        if (TransactionStatus.DENIED.equals(newStatus)) {
            if (TransactionType.DEPOSIT.equals(transactionType)) {
                wallet.setBalance(wallet.getBalance() - amount);
            } else {
                wallet.setUsableBalance(wallet.getBalance() + amount);
            }
        } else if (TransactionStatus.APPROVED.equals(newStatus)) {
            if (TransactionType.DEPOSIT.equals(transactionType)) {
                wallet.setUsableBalance(wallet.getBalance());
            } else {
                wallet.setBalance(wallet.getUsableBalance() );
            }
        }
    }

    private boolean customerCheck(Authentication auth, Long customerId) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE")) ||
                userAuthService.isCustomerOwn(userDetails.getUsername(), customerId)) {
            return true;
        }
        throw new CustomerOperationException("Customer not allowed to do operation for another customer!");
    }
}