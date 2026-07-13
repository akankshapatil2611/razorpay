package com.gayeway.Razorpay.payment.gateway.adapter;

import com.gayeway.Razorpay.payment.gateway.PaymentAdapter;
import com.gayeway.Razorpay.payment.gateway.dto.PaymentRequest;
import com.gayeway.Razorpay.payment.gateway.dto.PaymentResult;
import com.gayeway.Razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.gayeway.Razorpay.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class CardPaymentAdapter implements PaymentAdapter {

    private final VaultService vaultService;

    @Override
    public PaymentResult initiate(PaymentRequest paymentRequest) {
        String token = (String) paymentRequest.methodDetails().get("token");

        PaymentProcessorResponse response = vaultService.charge(
                paymentRequest.paymentId(), token, paymentRequest.amount(), paymentRequest.methodDetails()
        );

        return switch (response) {
            case PaymentProcessorResponse.Success success -> new PaymentResult.success(success.bankReference());
            case PaymentProcessorResponse.failure failure -> new PaymentResult.Failure(failure.errorCode(), failure.errorDescription());
            case PaymentProcessorResponse.Pending pending -> new PaymentResult.Pending(pending.processorRef());
        };

    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return null;
    }
}
