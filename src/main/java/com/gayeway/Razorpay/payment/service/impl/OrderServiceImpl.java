package com.gayeway.Razorpay.payment.service.impl;

import com.gayeway.Razorpay.common.enums.OrderStatus;
import com.gayeway.Razorpay.common.exception.BusinessRuleViolation;
import com.gayeway.Razorpay.common.exception.DuplicateResourceException;
import com.gayeway.Razorpay.common.exception.ResourceNotFoundException;
import com.gayeway.Razorpay.payment.dto.request.CreateOrderRequest;
import com.gayeway.Razorpay.payment.dto.response.OrderResponse;
import com.gayeway.Razorpay.payment.dto.response.PaymentResponse;
import com.gayeway.Razorpay.payment.entity.OrderRecord;
import com.gayeway.Razorpay.payment.entity.Payment;
import com.gayeway.Razorpay.payment.mapper.OrderMapper;
import com.gayeway.Razorpay.payment.mapper.PaymentMapper;
import com.gayeway.Razorpay.payment.repository.OrderRepository;
import com.gayeway.Razorpay.payment.repository.PaymentRepository;
import com.gayeway.Razorpay.payment.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final PaymentRepository paymentRepository;
    private  final PaymentMapper paymentMapper;

    @Value("${payment.order.default-order-expiry-minutes:30}")
    private int defaultOrderExpiryMinutes;

    @Transactional
    @Override
    public OrderResponse create(UUID merchantId, CreateOrderRequest request) {

        if(request.receipt() != null && orderRepository.existsByMerchantIdAndReceipt(merchantId, request.receipt())){
            throw new DuplicateResourceException("ORDER_RECEIPT_DUPLICATE", "Order with receipt already exists: "+request.receipt());
        }

        OrderRecord orderRecord = OrderRecord.builder()
                .receipt(request.receipt())
                .amount(request.amount())
                .notes(request.notes())

                .merchantId(merchantId)
                .orderstatus(OrderStatus.CREATED)
                .expiresAt(request.expiresAt()!=null ? request.expiresAt() : LocalDateTime.now().plusMinutes(defaultOrderExpiryMinutes))
                .build();

        orderRecord = orderRepository.save(orderRecord);

        // TODO - Publish kafka as order created

        return orderMapper.toResponse(orderRecord);
    }

    @Override
    public OrderResponse getById(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(orderId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));
        return orderMapper.toResponse(order);
    }

    @Transactional
    @Override
    public OrderResponse cancel(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(orderId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        if(order.getOrderstatus() == OrderStatus.CREATED || order.getOrderstatus() == OrderStatus.PAID)
        {
            throw new BusinessRuleViolation("ORDER_CANNOT_CANCEL", "cannot cancel order with status: "+order.getOrderstatus().name());
        }
        order.setOrderstatus(OrderStatus.CANCELLED);
        order = orderRepository.save(order);
        return orderMapper.toResponse(order);
    }

    @Transactional
    @Override
    public List<PaymentResponse> listPayments(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(orderId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));
        List<Payment> paymentList = paymentRepository.findByOrder_Id(order);

        return paymentMapper.toResponseList(paymentList);
    }
}
