package org.digitalwallet.app.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DepositRequest extends TransactionRequest {
    private String source;
}
