package com.gayeway.Razorpay.merchant.service.impl;

import com.gayeway.Razorpay.common.exception.ResourceNotFoundException;
import com.gayeway.Razorpay.common.util.RandomUtil;
import com.gayeway.Razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.gayeway.Razorpay.merchant.dto.response.APiKeyResponse;
import com.gayeway.Razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.gayeway.Razorpay.merchant.entity.ApiKey;
import com.gayeway.Razorpay.merchant.entity.Merchant;
import com.gayeway.Razorpay.merchant.mapper.ApiKeyMapper;
import com.gayeway.Razorpay.merchant.repository.ApiKeyRepository;
import com.gayeway.Razorpay.merchant.repository.MerchantRepository;
import com.gayeway.Razorpay.merchant.service.ApiKeyService;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyServiceImpl implements ApiKeyService {

    private final MerchantRepository merchantRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyMapper apiKeyMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ApiKeyCreateResponse create(UUID merchantId, CreateApiKeyRequest request) {

        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("merchant", merchantId));

        String key = "rzp_"+request.environment().name().toUpperCase() + RandomUtil.randomBase64(24);
        String rawSecret = RandomUtil.randomBase64(40);

        ApiKey apiKey = ApiKey.builder()
                .merchant(merchant)
                .keyId(key)
                .keySecretHash(passwordEncoder.encode(rawSecret))
                .environment(request.environment())
                .build();

        apiKey = apiKeyRepository.save(apiKey);

        return new ApiKeyCreateResponse(apiKey.getId(), key, rawSecret, request.environment());
    }

    @Override
    public List<APiKeyResponse> listOfMerchant(UUID merchantId) {
        List<ApiKey> apiKeys = apiKeyRepository.findByMerchant_Id(merchantId);
//        return apiKeys.stream()
//                .map(apiKey -> new APiKeyResponse(apiKey.getId(),
//                        apiKey.getKeyId(),
//                        apiKey.getEnvironment(),
//                        apiKey.isEnabled(),
//                        apiKey.getLastUsedAt(), null))
//                .toList();
        return apiKeyMapper.toApiResponse(apiKeys);
    }

    // delete api
    @Transactional
    @Override
    public void revoke(UUID merchantId, UUID keyId) {
        ApiKey apiKey = apiKeyRepository.findById(keyId)
                .filter(k -> k.getMerchant().getId().equals(merchantId))
                .orElseThrow(() -> new ResourceNotFoundException("Apikey ", keyId));
        apiKey.setEnabled(false);
        apiKeyRepository.save(apiKey);
    }

    @Transactional
    @Nullable
    @Override
    public ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId) {

        ApiKey apiKey = apiKeyRepository.findById(keyId)
                .filter(k -> k.getMerchant().getId().equals(merchantId))
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey", keyId));
        String newRawSecret = RandomUtil.randomBase64(40);
        apiKey.setPreviousKeySecretHash(apiKey.getKeySecretHash());
        apiKey.setKeySecretHash(passwordEncoder.encode(newRawSecret));
        apiKey.setRotatedAt(LocalDateTime.now());
        apiKey.setGracePeriodExpiresAt(LocalDateTime.now().plusHours(24));
        apiKey = apiKeyRepository.save(apiKey);

        return new ApiKeyCreateResponse(apiKey.getId(), apiKey.getKeyId(),
                newRawSecret, apiKey.getEnvironment());
    }


}
