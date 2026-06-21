package com.gayeway.Razorpay.payment.entity;

import com.gayeway.Razorpay.common.entity.BaseEntity;
import com.gayeway.Razorpay.common.entity.Money;
import com.gayeway.Razorpay.common.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "order_record", indexes = {
        @Index(name = "idx_order_id_merchant_id", columnList = "id, merchant_id"),
        @Index(name = "idx_order_merchant_id", columnList = "merchant_id")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="merchant_id", nullable = false)
    private UUID merchantId;

    @Embedded
    private Money amount;

    @Column(length = 100)
    private String receipt;

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
