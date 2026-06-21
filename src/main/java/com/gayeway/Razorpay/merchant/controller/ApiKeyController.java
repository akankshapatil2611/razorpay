package com.gayeway.Razorpay.merchant.controller;

import com.gayeway.Razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.gayeway.Razorpay.merchant.dto.response.APiKeyResponse;
import com.gayeway.Razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.gayeway.Razorpay.merchant.service.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/merchants/{merchantId}/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping
    public ResponseEntity<ApiKeyCreateResponse> create(@PathVariable UUID merchantId,
                                                       @Valid @RequestBody CreateApiKeyRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiKeyService.create(merchantId,request));
    }

    @GetMapping
    public ResponseEntity<List<APiKeyResponse>> listByMerchant(@PathVariable UUID merchantId)
    {
        return ResponseEntity.ok(apiKeyService.listOfMerchant(merchantId));
    }

    @DeleteMapping("/{keyId}")
    public ResponseEntity<Void> revoke(@PathVariable UUID merchantId, @PathVariable UUID keyId)
    {
        apiKeyService.revoke(merchantId, keyId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{keyId}/rotate")
    public ResponseEntity<ApiKeyCreateResponse> rotateKey(@PathVariable UUID merchantId, @PathVariable UUID keyId) {
        return ResponseEntity.ok(apiKeyService.rotate(merchantId, keyId));
    }
}
