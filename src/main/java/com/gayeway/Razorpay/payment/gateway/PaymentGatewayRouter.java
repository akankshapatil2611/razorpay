package com.gayeway.Razorpay.payment.gateway;

import com.gayeway.Razorpay.common.enums.PaymentMethod;
import com.gayeway.Razorpay.payment.gateway.dto.PaymentRequest;
import com.gayeway.Razorpay.payment.gateway.dto.PaymentResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentGatewayRouter {

    private final Map<PaymentMethod, PaymentAdapter> paymentAdapterMap;

    public PaymentResult initiate(PaymentRequest request){
        PaymentAdapter adapter = paymentAdapterMap.get(request.method());
        if(adapter == null){
            throw new IllegalArgumentException("No payment adapter registered for method: "+request.method());
        }
        return adapter.initiate(request);
    }

    public PaymentResult capture(PaymentMethod method, UUID paymentId) {
        PaymentAdapter adapter = paymentAdapterMap.get(method);
        if (adapter == null) {
            throw new IllegalArgumentException("No payment adapter registered for method: "+method);
        }
        return adapter.capture(paymentId);
    }
}
