package com.gayeway.Razorpay.payment.Config;

import com.gayeway.Razorpay.common.enums.PaymentMethod;
import com.gayeway.Razorpay.payment.gateway.PaymentAdapter;
import com.gayeway.Razorpay.payment.gateway.adapter.CardPaymentAdapter;
import com.gayeway.Razorpay.payment.gateway.adapter.NetBankingAdapter;
import com.gayeway.Razorpay.payment.gateway.adapter.UpiPaymentAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class PaymentAdapterConfig {

    @Bean
    public Map<PaymentMethod, PaymentAdapter> paymentAdapterMap() {
        return Map.of(
                PaymentMethod.CARD, new CardPaymentAdapter(),
                PaymentMethod.NETBANKING, new NetBankingAdapter(),
                PaymentMethod.UPI, new UpiPaymentAdapter()
        );
    }
}
