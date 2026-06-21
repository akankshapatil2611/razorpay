package com.gayeway.Razorpay.merchant.mapper;

import com.gayeway.Razorpay.merchant.dto.response.APiKeyResponse;
import com.gayeway.Razorpay.merchant.entity.ApiKey;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ApiKeyMapper {

    List<APiKeyResponse> toApiResponse(List<ApiKey> apiKeys);
}
