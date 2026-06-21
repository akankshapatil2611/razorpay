package com.gayeway.Razorpay.payment.controller;

import com.gayeway.Razorpay.payment.dto.request.CreateOrderRequest;
import com.gayeway.Razorpay.payment.dto.response.OrderResponse;
import com.gayeway.Razorpay.payment.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    UUID merchantId =UUID.fromString("1a426fb7-4287-4f96-97aa-fd85fd3b5659");

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.create(merchantId, request));
    }

}
