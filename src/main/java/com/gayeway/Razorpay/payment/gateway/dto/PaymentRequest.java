package com.gayeway.Razorpay.payment.gateway.dto;

import com.gayeway.Razorpay.common.entity.Money;
import com.gayeway.Razorpay.common.enums.PaymentMethod;

import java.util.Map;
import java.util.UUID;

public record PaymentRequest(

        UUID paymentId,
        UUID orderId,
        UUID merchantId,
        Money amount,
        PaymentMethod method,
        Map<String, Object> methodDetails
) {
}
