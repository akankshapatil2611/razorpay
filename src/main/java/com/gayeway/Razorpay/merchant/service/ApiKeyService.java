package com.gayeway.Razorpay.merchant.service;

import com.gayeway.Razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.gayeway.Razorpay.merchant.dto.response.ApiKeyCreateResponse;

import java.util.UUID;

public interface ApiKeyService {
    ApiKeyCreateResponse create(UUID merchantId, CreateApiKeyRequest request);
}
