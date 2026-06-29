package com.gayeway.Razorpay.payment.processor;

import com.gayeway.Razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.gayeway.Razorpay.payment.processor.dto.PaymentProcessorResponse;

public interface PaymentProcessor {

    PaymentProcessorResponse charge(PaymentProcessorRequest request);
}
