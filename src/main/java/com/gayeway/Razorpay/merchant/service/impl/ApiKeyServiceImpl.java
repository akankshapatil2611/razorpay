package com.gayeway.Razorpay.merchant.service.impl;

import com.gayeway.Razorpay.common.exception.ResourceNotFoundException;
import com.gayeway.Razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.gayeway.Razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.gayeway.Razorpay.merchant.entity.Merchant;
import com.gayeway.Razorpay.merchant.repository.ApiKeyRepository;
import com.gayeway.Razorpay.merchant.repository.MerchantRepository;
import com.gayeway.Razorpay.merchant.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyServiceImpl implements ApiKeyService {

    private final MerchantRepository merchantRepository;
    private ApiKeyRepository apiKeyRepository;

    @Override
    public ApiKeyCreateResponse create(UUID merchantId, CreateApiKeyRequest request) {

        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("merchant", merchantId));

        return null;
    }
}
