package com.gayeway.Razorpay.payment.processor.dto;

public sealed interface PaymentProcessorResponse permits
        PaymentProcessorResponse.Pending,
        PaymentProcessorResponse.Success,
        PaymentProcessorResponse.failure
{
    record Pending(String processorRef) implements PaymentProcessorResponse{}

    record Success(String processorRef, String bankReference) implements PaymentProcessorResponse {}

    record failure(String errorCode, String errorDescription) implements PaymentProcessorResponse {}
}
