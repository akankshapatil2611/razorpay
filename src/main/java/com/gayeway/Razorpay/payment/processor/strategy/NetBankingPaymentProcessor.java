package com.gayeway.Razorpay.payment.processor.strategy;

import com.gayeway.Razorpay.common.util.RandomUtil;
import com.gayeway.Razorpay.payment.processor.PaymentProcessor;
import com.gayeway.Razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.gayeway.Razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NetBankingPaymentProcessor implements PaymentProcessor {


    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        // call 3rd party

        final String BANK_CODE_FAIL  = "BANK_CODE_FAIL";

        String bankCode = request.methodDetails() != null ?
                request.methodDetails().get("BANK").toString() : null;

        if(BANK_CODE_FAIL.equals(bankCode)){
            return new PaymentProcessorResponse.failure("BANK_REJECTED",
                    "Banked rejected the transaction registration");
        }

        String processorRef = "NBK_PROCESSOR_"+ RandomUtil.randomBase64(16);

        String redirectRef = "http://REDIRECT_BANK.com/"+processorRef;

        return new PaymentProcessorResponse.Success(processorRef, redirectRef);
    }
}
