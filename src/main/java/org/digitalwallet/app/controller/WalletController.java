package org.digitalwallet.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.digitalwallet.app.dto.WalletDTO;
import org.digitalwallet.app.dto.request.*;
import org.digitalwallet.app.service.TransactionService;
import org.digitalwallet.app.service.WalletService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
@Tag(name = "Digital Wallet API", description = "Digital Wallet API")
@AllArgsConstructor
public class WalletController {
    private final WalletService walletService;
    private final TransactionService transactionService;

    @PostMapping
    @Operation(description = "Create a new wallet with given details")
    public void createWallet(@Valid @RequestBody WalletRequest walletRequest, Authentication auth) {
        walletService.createWallet(walletRequest, auth);
    }

    @GetMapping("/{customerId}")
    @Operation(description = "List wallets for a given customer")
    public List<WalletDTO> listWallets(@PathVariable Long customerId,
                                       @ModelAttribute WalletFilterRequest filter,
                                       Authentication auth) {
        return walletService.listWallets(customerId, filter, auth);
    }

    @PostMapping("/{walletId}/deposit")
    @Operation(description = "Make deposit with given details")
    public void deposit(@PathVariable Long walletId,
                        @RequestBody DepositRequest depositRequest,
                        Authentication auth) {
        walletService.deposit(walletId, depositRequest, auth);
    }

    @PostMapping("/{walletId}/withdraw")
    @Operation(description = "Make a withdraw with given details")
    public void withdraw(@PathVariable Long walletId,
                         @RequestBody WithdrawRequest withdrawRequest,
                         Authentication auth) {
        walletService.withdraw(walletId, withdrawRequest, auth);
    }

    @GetMapping("/{walletId}/transactions")
    @Operation(description = "List transactions for a given wallet")
    public void listTransactions(@PathVariable Long walletId) {
        transactionService.listTransactions(walletId);
    }

    @PostMapping("/transactions")
    @Operation(description = "Approve or deny a transaction with given details")
    public void progressTransaction(@RequestBody TransactionOperationRequest transactionOperationRequest, Authentication auth) {
        walletService.progressTransaction(transactionOperationRequest, auth);
    }


}