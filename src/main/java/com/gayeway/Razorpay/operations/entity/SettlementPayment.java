package com.gayeway.Razorpay.operations.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "settlement_payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettlementPayment {

    @EmbeddedId
    private SettlementPaymentId id;

    @MapsId()                        // maps with primary key from settlement
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "settlement_id", nullable = false)
    private Settlement settlement;
}
