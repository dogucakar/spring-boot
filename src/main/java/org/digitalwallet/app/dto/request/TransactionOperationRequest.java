package org.digitalwallet.app.dto.request;

import lombok.Data;
import org.digitalwallet.app.enumeration.TransactionStatus;

@Data
public class TransactionOperationRequest {
    private Long transactionId;
    private TransactionStatus status;
}