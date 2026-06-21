package com.gayeway.Razorpay.merchant.dto.request;

import com.gayeway.Razorpay.common.enums.Environment;

public record CreateApiKeyRequest(
        Environment environment
) {
}
