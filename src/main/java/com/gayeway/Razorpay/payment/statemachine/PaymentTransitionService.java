package com.gayeway.Razorpay.payment.statemachine;

import com.gayeway.Razorpay.common.enums.PaymentActor;
import com.gayeway.Razorpay.common.enums.PaymentEvent;
import com.gayeway.Razorpay.common.enums.PaymentStatus;
import com.gayeway.Razorpay.payment.entity.Payment;
import com.gayeway.Razorpay.payment.entity.PaymentTrasitionLog;
import com.gayeway.Razorpay.payment.repository.PaymentTransitionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentTransitionService {

    private final PaymentTransitionLogRepository paymentTransitionLogRepository;
    private final PaymentStateMachine paymentStateMachine;

    public PaymentStatus apply(Payment payment, PaymentEvent event) {
        PaymentStatus next = paymentStateMachine.transition(payment.getStatus(), event);
        payment.setStatus(next);
        PaymentTrasitionLog log = PaymentTrasitionLog.builder()
                .payment(payment)
                .from_status(payment.getStatus())
                .event_type(event)
                .to_status(next)
                .actor(PaymentActor.SYSTEM) //TODO: fetch merchant context to identify actor
                .occurred_at(LocalDateTime.now())
                .build();

        paymentTransitionLogRepository.save(log);
        return next;
    }
}
