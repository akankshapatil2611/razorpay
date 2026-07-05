package com.gayeway.Razorpay.payment.gateway.dto;

import com.gayeway.Razorpay.payment.gateway.PaymentAdapter;

public sealed interface PaymentResult permits
        PaymentResult.Pending,
        PaymentResult.success,
        PaymentResult.Failure
{

    record Pending(String registrationRef) implements PaymentResult {}
    record success(String bankRef) implements PaymentResult {}
    record Failure(String errorCode, String errorDescription) implements PaymentResult {}
}
