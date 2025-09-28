package org.digitalwallet.app.dto;

import lombok.Data;

@Data
public class WalletDTO {
    private Long id;
    private Long customerId;
    private String walletName;
    private String currency;
    private boolean activeForShopping;
    private boolean activeForWithdraw;
    private Double balance;
    private Double usableBalance;
}
