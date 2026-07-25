package com.gayeway.Razorpay.merchant.cache;

import com.gayeway.Razorpay.common.enums.Environment;

import java.time.LocalDateTime;
import java.util.UUID;

public record ApiKeyCacheEntry(
        String keyId,
        String keySecretHash,
        String previousKeySecretHash,
        LocalDateTime gracePeriodExpiresAt,
        UUID merchantId,
        Environment environment,
        boolean enabled
) {

    // for rotate-key grace window
    public boolean isInGracePeriod(){
        return gracePeriodExpiresAt != null && LocalDateTime.now().isBefore(gracePeriodExpiresAt);
    }
}
