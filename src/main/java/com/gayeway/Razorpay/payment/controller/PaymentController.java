package com.gayeway.Razorpay.payment.controller;

import com.gayeway.Razorpay.payment.dto.request.PaymentInitRequest;
import com.gayeway.Razorpay.payment.dto.response.PaymentResponse;
import com.gayeway.Razorpay.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    UUID merchanId = UUID.fromString("1a426fb7-4287-4f96-97aa-fd85fd3b5659");

    @PostMapping
    public ResponseEntity<PaymentResponse> initiate(@RequestBody PaymentInitRequest paymentInitRequest)
    {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.initiate(merchanId, paymentInitRequest));
    }
}
