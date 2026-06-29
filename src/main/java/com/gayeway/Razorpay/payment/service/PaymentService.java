package com.gayeway.Razorpay.payment.service;

import com.gayeway.Razorpay.payment.dto.request.PaymentInitRequest;
import com.gayeway.Razorpay.payment.dto.response.PaymentResponse;

import java.util.UUID;

public interface PaymentService {

    PaymentResponse initiate(UUID merchantId, PaymentInitRequest request);
}
