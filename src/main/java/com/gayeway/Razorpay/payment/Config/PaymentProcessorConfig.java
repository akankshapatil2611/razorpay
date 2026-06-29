package com.gayeway.Razorpay.payment.Config;

import com.gayeway.Razorpay.common.enums.PaymentMethod;
import com.gayeway.Razorpay.payment.processor.PaymentProcessor;
import com.gayeway.Razorpay.payment.processor.strategy.CardPaymentProcessor;
import com.gayeway.Razorpay.payment.processor.strategy.NetBankingPaymentProcessor;
import com.gayeway.Razorpay.payment.processor.strategy.UpiPaymentProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class PaymentProcessorConfig {

    @Bean
    public Map<PaymentMethod, PaymentProcessor> paymentProcessorMap()
    {
        return Map.of(
                PaymentMethod.CARD, new CardPaymentProcessor(),
                PaymentMethod.UPI, new UpiPaymentProcessor(),
                PaymentMethod.NETBANKING, new NetBankingPaymentProcessor()
        );

    }
}
