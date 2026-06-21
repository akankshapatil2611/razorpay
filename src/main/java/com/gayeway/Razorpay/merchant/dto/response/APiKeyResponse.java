package com.gayeway.Razorpay.merchant.dto.response;

import com.gayeway.Razorpay.common.enums.Environment;

import java.time.LocalDateTime;
import java.util.UUID;

public record APiKeyResponse(
        UUID id,
        String keyId,
        Environment environment,
        boolean enabled,
        LocalDateTime lastUsedAt,
        LocalDateTime createdAt

) {
}
