package com.gayeway.Razorpay.payment.dto.response;

import com.gayeway.Razorpay.common.entity.Money;
import com.gayeway.Razorpay.common.enums.PaymentMethod;
import com.gayeway.Razorpay.common.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        UUID orderId,
        UUID merchantId,
        Money amount,
        PaymentStatus status,
        PaymentMethod method,
        Map<String, Object> methodDetails,
        String errorCode,
        String errorDescription,
        LocalDateTime capturedAt,
        LocalDateTime createdAt
) {
}
