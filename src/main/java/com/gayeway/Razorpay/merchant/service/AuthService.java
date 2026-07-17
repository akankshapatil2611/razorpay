package com.gayeway.Razorpay.merchant.service;

import com.gayeway.Razorpay.merchant.dto.request.LoginRequest;
import com.gayeway.Razorpay.merchant.dto.request.MerchantSignupRequest;
import com.gayeway.Razorpay.merchant.dto.response.LoginResponse;
import com.gayeway.Razorpay.merchant.dto.response.MerchantResponse;

public interface AuthService {

    public MerchantResponse signup(MerchantSignupRequest request);

    public  LoginResponse login(LoginRequest request);
}
