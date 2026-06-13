package com.gayeway.Razorpay.merchant.controller;

import com.gayeway.Razorpay.merchant.dto.request.MerchantSignupRequest;
import com.gayeway.Razorpay.merchant.dto.response.MerchantResponse;
import com.gayeway.Razorpay.merchant.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<MerchantResponse> signup(@RequestBody @Valid MerchantSignupRequest request){
        MerchantResponse merchantResponse = authService.signup(request);
        return new ResponseEntity<>(merchantResponse, HttpStatus.CREATED);
    }
}
