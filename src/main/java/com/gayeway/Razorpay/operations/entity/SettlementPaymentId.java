package com.gayeway.Razorpay.operations.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SettlementPaymentId implements Serializable {

    @Column(name = "settlement_id", nullable = false)
    private UUID settlementId;

    @Column(name = "payment_id", nullable = false)
    private UUID paymentId;
}
