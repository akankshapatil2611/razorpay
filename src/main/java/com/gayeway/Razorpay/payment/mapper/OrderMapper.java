package com.gayeway.Razorpay.payment.mapper;

import com.gayeway.Razorpay.payment.dto.response.OrderResponse;
import com.gayeway.Razorpay.payment.entity.OrderRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    @Mapping(source = "orderstatus", target = "status")
    OrderResponse toResponse(OrderRecord orderRecord);
}
