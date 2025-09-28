package org.digitalwallet.app.repository;

import org.digitalwallet.app.enumeration.Currency;
import org.digitalwallet.app.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class WalletRepositoryTest {
    @Autowired
    private WalletRepository walletRepository;

    @Test
    void findByCustomerId_Successful() {
        var wallet = TestUtil.createWallet(null);
        var savedEntity = walletRepository.save(wallet);
        var wallets = walletRepository.findByCustomerId(1L);
        assertEquals(1, wallets.size());
        assertEquals(savedEntity.getId(), wallets.getFirst().getId());
    }

    @Test
    void findByCustomerIdAndCurrencyAndWalletName_Successful() {
        var wallet = TestUtil.createWallet(null);
        var savedEntity = walletRepository.save(wallet);
        var wallets = walletRepository.findByCustomerIdAndCurrencyAndWalletName(1L, Currency.EUR.toString(), "TEST");
        assertEquals(1, wallets.size());
        assertEquals(savedEntity.getId(), wallets.getFirst().getId());
        assertEquals(1L, wallets.getFirst().getCustomerId());
        assertEquals(Currency.EUR.toString(), wallets.getFirst().getCurrency());
        assertEquals("TEST", wallets.getFirst().getWalletName());
    }
}