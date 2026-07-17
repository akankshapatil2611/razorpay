package com.gayeway.Razorpay.vault.controller;

import com.gayeway.Razorpay.merchant.security.MerchantContext;
import com.gayeway.Razorpay.vault.dto.Request.TokenizeRequest;
import com.gayeway.Razorpay.vault.dto.Response.TokenizeResponse;
import com.gayeway.Razorpay.vault.service.VaultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/vault")
public class VaultController {

    private final VaultService vaultService;
    private final MerchantContext merchantContext;


    @PostMapping("/tokenize")
    public ResponseEntity<TokenizeResponse> tokenize(@Valid @RequestBody TokenizeRequest tokenizeRequest)
    {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vaultService.tokenize(tokenizeRequest, merchantContext.getMerchantId()));
    }
}
