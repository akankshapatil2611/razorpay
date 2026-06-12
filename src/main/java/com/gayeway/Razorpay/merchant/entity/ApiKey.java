package com.gayeway.Razorpay.merchant.entity;

import com.gayeway.Razorpay.common.enums.Environment;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="api_key")
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="merchant_id", nullable = false)
    private Merchant merchant;

    private String keyId;

    private String keySecretHash;

    private Environment environment;

    private boolean enabled=true;

    private LocalDateTime lastUsedAt;

    private LocalDateTime rotatedAt;

    private LocalDateTime gracePeriodExpiresAt;
}
