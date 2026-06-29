package com.gayeway.Razorpay.payment.mapper;

import com.gayeway.Razorpay.payment.dto.response.PaymentResponse;
import com.gayeway.Razorpay.payment.entity.Payment;
import com.gayeway.Razorpay.payment.repository.PaymentRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {

    @Mapping(target = "orderId", source = "order.id")
    List<PaymentResponse> toResponseList(List<Payment> paymentList);

    @Mapping(target = "orderId", source = "order.id")
    PaymentResponse toResponse(Payment payment);

}
