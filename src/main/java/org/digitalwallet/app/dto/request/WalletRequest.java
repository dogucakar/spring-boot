package org.digitalwallet.app.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class WalletRequest {
    private Long customerId;
    private String walletName;
    @Pattern(
            regexp = "^(TRY|USD|EUR)$",
            message = "Currency must be one of: TRY, USD, EUR"
    )
    private String currency;
    private boolean activeForShopping;
    private boolean activeForWithdraw;
}
