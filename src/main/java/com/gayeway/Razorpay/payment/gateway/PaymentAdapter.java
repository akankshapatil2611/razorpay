package com.gayeway.Razorpay.payment.gateway;

import com.gayeway.Razorpay.payment.gateway.dto.PaymentRequest;
import com.gayeway.Razorpay.payment.gateway.dto.PaymentResult;

public interface PaymentAdapter {

    PaymentResult initiate(PaymentRequest paymentRequest);
}
