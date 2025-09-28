package org.digitalwallet.app.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WithdrawRequest extends TransactionRequest {
    private String destination;
}