package com.gayeway.Razorpay.payment.service;

import com.gayeway.Razorpay.payment.dto.request.CreateOrderRequest;
import com.gayeway.Razorpay.payment.dto.response.OrderResponse;
import com.gayeway.Razorpay.payment.dto.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponse create(UUID merchantId, CreateOrderRequest request);

    OrderResponse getById(UUID merchantId, UUID orderId);

    OrderResponse cancel(UUID merchantId, UUID orderId);

    List<PaymentResponse> listPayments(UUID merchantId, UUID orderId);
}
