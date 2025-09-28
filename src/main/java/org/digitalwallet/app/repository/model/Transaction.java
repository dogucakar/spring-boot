package org.digitalwallet.app.repository.model;

import jakarta.persistence.*;
import lombok.Data;
import org.digitalwallet.app.enumeration.OppositePartyType;
import org.digitalwallet.app.enumeration.TransactionStatus;
import org.digitalwallet.app.enumeration.TransactionType;

@Data
@Entity
@Table(name = "TRANSACTION")
public class Transaction {
    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "WALLET_ID")
    private Long walletId;

    @Column(name = "AMOUNT")
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "OPPOSITE_PARTY_TYPE")
    private OppositePartyType oppositePartyType;

    @Column(name = "OPPOSITE_PARTY")
    private String oppositeParty;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private TransactionStatus status;
}
