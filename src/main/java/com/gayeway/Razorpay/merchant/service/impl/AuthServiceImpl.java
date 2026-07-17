package com.gayeway.Razorpay.merchant.service.impl;

import com.gayeway.Razorpay.common.enums.MerchantStatus;
import com.gayeway.Razorpay.common.enums.UserRole;
import com.gayeway.Razorpay.common.exception.DuplicateResourceException;
import com.gayeway.Razorpay.common.exception.ResourceNotFoundException;
import com.gayeway.Razorpay.merchant.dto.request.LoginRequest;
import com.gayeway.Razorpay.merchant.dto.request.MerchantSignupRequest;
import com.gayeway.Razorpay.merchant.dto.response.LoginResponse;
import com.gayeway.Razorpay.merchant.dto.response.MerchantResponse;
import com.gayeway.Razorpay.merchant.entity.AppUser;
import com.gayeway.Razorpay.merchant.entity.Merchant;
import com.gayeway.Razorpay.merchant.mapper.MerchantMapper;
import com.gayeway.Razorpay.merchant.repository.AppUserRepository;
import com.gayeway.Razorpay.merchant.repository.MerchantRepository;
import com.gayeway.Razorpay.merchant.security.JwtUtil;
import com.gayeway.Razorpay.merchant.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final MerchantRepository merchantRepository;
    private final AppUserRepository appUserRepository;
    private MerchantMapper merchantMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    @Override
    @Transactional
    public MerchantResponse signup(MerchantSignupRequest request) {
        if(merchantRepository.existsByEmail(request.email())){
            throw new DuplicateResourceException("DUPLICATE_MERCHANT_ERROR","Merchant with email already exists "+ request.email());
        }
        Merchant merchant = merchantMapper.toMerchantEntity(request);
        merchant.setMerchantStatus(MerchantStatus.PENDING_KYC);
        merchant = merchantRepository.save(merchant);

        AppUser appUser = AppUser.builder()
                .email(request.email())
                .merchant(merchant)
                .passwordHash(request.password())  // TODO : encrypth using bcrrypt
                .role(UserRole.OWNER)
                .build();
        appUserRepository.save(appUser);

        return merchantMapper.toMerchantResponse(merchant);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        AppUser appUser = appUserRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.email()));

        String token = jwtUtil.generateAccessToken(request.email(), appUser.getMerchant().getId(), appUser.getRole().toString());

        return new LoginResponse(token);
    }
}
