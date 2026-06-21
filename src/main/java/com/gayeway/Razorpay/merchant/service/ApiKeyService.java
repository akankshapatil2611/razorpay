package com.gayeway.Razorpay.merchant.service;

import com.gayeway.Razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.gayeway.Razorpay.merchant.dto.response.APiKeyResponse;
import com.gayeway.Razorpay.merchant.dto.response.ApiKeyCreateResponse;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.UUID;

public interface ApiKeyService {
    ApiKeyCreateResponse create(UUID merchantId, CreateApiKeyRequest request);

    List<APiKeyResponse> listOfMerchant(UUID merchantId);

    public void revoke(UUID merchantId, UUID keyId);

    public @Nullable ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId);
}
