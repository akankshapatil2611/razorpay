package com.gayeway.Razorpay.operations.entity;

import com.gayeway.Razorpay.common.entity.BaseEntity;
import com.gayeway.Razorpay.payment.entity.Payment;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "settlement_payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettlementPayment extends BaseEntity {

    @EmbeddedId
    private SettlementPaymentId id;

    @MapsId("settlementId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "settlement_id", nullable = false)
    private Settlement settlement;

//    @MapsId("paymentId")
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "payment_id", nullable = false)
//    private Payment payment;
}
