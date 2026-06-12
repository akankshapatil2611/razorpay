package com.gayeway.Razorpay.operations.entity;

import com.gayeway.Razorpay.common.entity.Money;
import com.gayeway.Razorpay.common.enums.SettlementStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "settlement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID merchant_id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amountUnits", column = @Column(name = "gross_amount_units",  nullable = false)),
            @AttributeOverride(name="currency", column = @Column(name = "gross_amount_currency", nullable = false))
    })
    private Money grossAmount;    // total amount

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amountUnits", column = @Column(name = "refund_amount_units",  nullable = false)),
            @AttributeOverride(name="currency", column = @Column(name = "refund_amount_currency", nullable = false))
    })
    private Money refundAmount;   // refunded amount

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amountUnits", column = @Column(name = "fee_amount_units",  nullable = false)),
            @AttributeOverride(name="currency", column = @Column(name = "fee_amount_currency", nullable = false))
    })
    private Money feeAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amountUnits", column = @Column(name = "gst_amount_units",  nullable = false)),
            @AttributeOverride(name="currency", column = @Column(name = "gst_amount_currency", nullable = false))
    })
    private Money gstAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amountUnits", column = @Column(name = "net_amount_units",  nullable = false)),
            @AttributeOverride(name="currency", column = @Column(name = "net_amount_currency", nullable = false))
    })
    private Money netAmount;     // final amount

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SettlementStatus status;

    @Column(nullable = false, length = 50)
    private String bank_reference;

    private LocalDateTime processedAt;





}
