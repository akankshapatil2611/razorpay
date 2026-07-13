package com.gayeway.Razorpay.vault.service;

import com.gayeway.Razorpay.common.entity.Money;
import com.gayeway.Razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.gayeway.Razorpay.vault.dto.Request.TokenizeRequest;
import com.gayeway.Razorpay.vault.dto.Response.TokenizeResponse;

import java.util.Map;
import java.util.UUID;

public interface VaultService {

    public TokenizeResponse tokenize(TokenizeRequest tokenizeRequest, UUID merchantId);

    PaymentProcessorResponse charge(UUID paymentId, String token, Money amount, Map<String, Object> methodDetails);
}
