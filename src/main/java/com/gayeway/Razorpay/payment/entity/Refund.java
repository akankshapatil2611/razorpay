package com.gayeway.Razorpay.payment.entity;

import com.gayeway.Razorpay.common.entity.Money;
import com.gayeway.Razorpay.common.enums.RefundStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "refund")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(nullable = false)
    private Long merchant_id;

    @Embedded
    private Money amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundStatus status;

    @Column(length = 100)
    private String bank_reference;

    @Column(length = 100)
    private String error_code;

    @Column(length = 500)
    private String error_description;

    // merchant attach notes why they are refunding
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "notes")
    private Map<String, Object> notes;

    private LocalDateTime processed_at;

}
