package com.gayeway.Razorpay.payment.Config;

import com.gayeway.Razorpay.common.enums.PaymentMethod;
import com.gayeway.Razorpay.payment.gateway.PaymentAdapter;
import com.gayeway.Razorpay.payment.gateway.adapter.CardPaymentAdapter;
import com.gayeway.Razorpay.payment.gateway.adapter.NetBankingAdapter;
import com.gayeway.Razorpay.payment.gateway.adapter.UpiPaymentAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PaymentAdapterConfig {

    private final NetBankingAdapter netBankingAdapter;
    private final UpiPaymentAdapter upiPaymentAdapter;
    private final CardPaymentAdapter cardPaymentAdapter;

    @Bean
    public Map<PaymentMethod, PaymentAdapter> PaymentAdapterConfig() {
        return Map.of(
                PaymentMethod.CARD, cardPaymentAdapter,
                PaymentMethod.NETBANKING, netBankingAdapter,
                PaymentMethod.UPI, upiPaymentAdapter
        );
    }
}
