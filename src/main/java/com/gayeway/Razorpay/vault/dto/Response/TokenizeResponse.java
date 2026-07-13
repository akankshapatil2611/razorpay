package com.gayeway.Razorpay.vault.dto.Response;

import com.gayeway.Razorpay.common.enums.CardBrand;

public record TokenizeResponse(

        String token,
        String lastFour,
        CardBrand brand,
        Integer expiryMonth,
        Integer expiryYear
) {
}
