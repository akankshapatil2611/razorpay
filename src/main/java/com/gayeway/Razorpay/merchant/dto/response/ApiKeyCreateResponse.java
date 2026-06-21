package com.gayeway.Razorpay.merchant.dto.response;

import com.gayeway.Razorpay.common.enums.Environment;

import java.util.UUID;

public record ApiKeyCreateResponse(
        UUID id,
        String keyId,
        String keySecret,
        Environment environment
) {
}
