package com.gayeway.Razorpay.payment.processor.strategy;

import com.gayeway.Razorpay.common.util.RandomUtil;
import com.gayeway.Razorpay.payment.processor.PaymentProcessor;
import com.gayeway.Razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.gayeway.Razorpay.payment.processor.dto.PaymentProcessorResponse;

public class UpiPaymentProcessor implements PaymentProcessor {


    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {

        final String VPA_CODE_FAIL = "fail@okaxis";

        String bankCode = request.methodDetails() != null ?
                request.methodDetails().get("vpa").toString() : null;

        // simulation
        if (VPA_CODE_FAIL.equals(bankCode)) {
            return new PaymentProcessorResponse.failure("UPI_REJECTED",
                    "Banked rejected the transaction registration"
            );
        }

        String processorRef = "UPI_PROCESSOR_"+ RandomUtil.randomBase64(16);

        String bankRef = "BANK_REF"+RandomUtil.randomBase64(16);

        return new PaymentProcessorResponse.Success(processorRef, bankRef);

    }
}
