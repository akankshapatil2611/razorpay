package com.gayeway.Razorpay.payment.gateway.adapter;

import com.gayeway.Razorpay.payment.gateway.PaymentAdapter;
import com.gayeway.Razorpay.payment.gateway.dto.PaymentRequest;
import com.gayeway.Razorpay.payment.gateway.dto.PaymentResult;

import java.util.UUID;

public class CardPaymentAdapter implements PaymentAdapter {

    @Override
    public PaymentResult initiate(PaymentRequest paymentRequest) {
        return null;
    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return null;
    }
}
