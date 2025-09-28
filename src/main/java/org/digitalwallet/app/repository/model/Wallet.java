package org.digitalwallet.app.repository.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "WALLET")
public class Wallet {
    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "WALLET_NAME")
    private String walletName;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "ACTIVE_FOR_SHOPPING")
    private boolean activeForShopping;

    @Column(name = "ACTIVE_FOR_WITHDRAW")
    private boolean activeForWithdraw;

    @Column(name = "BALANCE")
    private Double balance = 0d;

    @Column(name = "USABLE_BALANCE")
    private Double usableBalance = 0d;
}
