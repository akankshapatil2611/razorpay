package com.gayeway.Razorpay.payment.entity;

import com.gayeway.Razorpay.common.entity.Money;
import com.gayeway.Razorpay.common.enums.OrderStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "order_record")
public class OrderRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="merchant_id", nullable = false)
    private UUID merchant_id;

    @Embedded
    private Money amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus orderstatus = OrderStatus.CREATED;

    @Column(nullable = false)
    private Integer attempts = 0;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> notes;       // ordernotes stores in the form of JSON

    @Column(nullable = false)
    private LocalDateTime expiresAt;
}
