package com.gayeway.Razorpay.payment.processor.dto;

import com.gayeway.Razorpay.common.entity.Money;
import com.gayeway.Razorpay.common.enums.PaymentMethod;

import java.util.Map;

public record PaymentProcessorRequest(
        PaymentMethod paymentMethod,
        Money amount,
        Map<String, Object> methodDetails
) {
}
