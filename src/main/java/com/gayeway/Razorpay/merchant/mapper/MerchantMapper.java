package com.gayeway.Razorpay.merchant.mapper;

import com.gayeway.Razorpay.merchant.dto.request.MerchantSignupRequest;
import com.gayeway.Razorpay.merchant.dto.response.MerchantResponse;
import com.gayeway.Razorpay.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MerchantMapper {
    Merchant toMerchantEntity(MerchantSignupRequest request);

    MerchantResponse toMerchantResponse(Merchant merchant);
}
