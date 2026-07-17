package com.gayeway.Razorpay.payment.gateway.adapter;

import com.gayeway.Razorpay.common.enums.PaymentMethod;
import com.gayeway.Razorpay.payment.gateway.PaymentAdapter;
import com.gayeway.Razorpay.payment.gateway.dto.PaymentRequest;
import com.gayeway.Razorpay.payment.gateway.dto.PaymentResult;
import com.gayeway.Razorpay.payment.processor.PaymentProcessorRouter;
import com.gayeway.Razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.gayeway.Razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpiPaymentAdapter implements PaymentAdapter {

    private final PaymentProcessorRouter paymentProcessorRouter;
    @Override
    public PaymentResult initiate(PaymentRequest paymentRequest) {

        log.info("Initiate Payment with UpiPaymentAdapter, payment : {}", paymentRequest.paymentId());

        try {
            PaymentProcessorRequest paymentProcessorRequest = PaymentProcessorRequest.noncard(
                    paymentRequest.paymentId(),
                    PaymentMethod.UPI,
                    paymentRequest.amount(),
                    paymentRequest.methodDetails()
            );

            PaymentProcessorResponse paymentProcessorResponse = paymentProcessorRouter.charge(paymentProcessorRequest);

            return switch (paymentProcessorResponse){
                case PaymentProcessorResponse.failure failure ->
                        new PaymentResult.Failure(failure.errorCode(), failure.errorDescription());

                case PaymentProcessorResponse.Pending  pending ->
                        new PaymentResult.Pending(pending.processorRef());

                case PaymentProcessorResponse.Success  success ->
                        new PaymentResult.success(success.bankReference());

            };

        }
        catch(Exception e) {
            log.warn("UPI payment failed, paymentId: {}", paymentRequest.paymentId(), e);
            return new PaymentResult.Failure("UPI_FAILED", e.getMessage());
        }

    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return new PaymentResult.success("UPI_REF");
    }
}
