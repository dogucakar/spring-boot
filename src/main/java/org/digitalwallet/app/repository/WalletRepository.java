package org.digitalwallet.app.repository;

import org.digitalwallet.app.repository.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    List<Wallet> findByCustomerId(Long customerId);
    List<Wallet> findByCustomerIdAndCurrencyAndWalletName(Long customerId, String currency, String walletName);
}
