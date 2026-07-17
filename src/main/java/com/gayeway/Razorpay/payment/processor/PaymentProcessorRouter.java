package com.gayeway.Razorpay.payment.processor;

import com.gayeway.Razorpay.common.enums.PaymentMethod;
import com.gayeway.Razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.gayeway.Razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentProcessorRouter {

    private final Map<PaymentMethod, PaymentProcessor> paymentProcessors;

    public PaymentProcessorResponse charge(PaymentProcessorRequest request)
    {
        PaymentProcessor paymentProcessor = paymentProcessors.get(request.paymentMethod());
        if(paymentProcessor==null){
            throw new IllegalArgumentException("No payment processor registered for method: "+request.paymentMethod());
        }
        return paymentProcessor.charge(request);
    }
}
