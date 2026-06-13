package com.gayeway.Razorpay.merchant.dto.response;

import com.gayeway.Razorpay.common.enums.BusinessType;
import com.gayeway.Razorpay.common.enums.MerchantStatus;

import java.util.UUID;

public record MerchantResponse(
        UUID id,
        String name,
        String email,
        String businessName,
        BusinessType businessType,
        MerchantStatus merchantStatus
) {
}
