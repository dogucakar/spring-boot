package org.digitalwallet.app.dto.request;

import lombok.Data;

@Data
public class WalletFilterRequest {
    private String currency;
    private String walletName;
}