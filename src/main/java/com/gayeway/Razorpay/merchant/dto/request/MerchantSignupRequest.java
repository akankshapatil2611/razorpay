package com.gayeway.Razorpay.merchant.dto.request;

import com.gayeway.Razorpay.common.enums.BusinessType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MerchantSignupRequest (

        @NotNull(message = "Name should be provided")
        @Size(max=50, message = "Name should be more than 50 character string")
        String name,

        @Email
        @NotNull(message = "Email is required")
        String email,

        @NotNull(message = "Password is required")
        @Size(min=8, message = "Password atleast should be 8 chracter long")
        String password,

        @Size(max=50, message = "business name should be more than 50 character")
        String businessName,
        BusinessType businessType

){

}
