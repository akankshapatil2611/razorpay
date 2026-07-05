package com.gayeway.Razorpay.payment.statemachine;

import com.gayeway.Razorpay.common.enums.PaymentEvent;
import com.gayeway.Razorpay.common.enums.PaymentStatus;
import com.gayeway.Razorpay.common.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentStateMachine {

    private record Transition(PaymentStatus from, PaymentEvent event) {}

//    Authorize = hold money
//    Capture = take money
//    Settle = pay merchant (minus fees)
    private static final Map<Transition, PaymentStatus> TRANSITION = Map.ofEntries(
        Map.entry(new Transition(PaymentStatus.CREATED, PaymentEvent.AUTHORIZE_ATTEMPT), PaymentStatus.AUTHORIZED),
        Map.entry(new Transition(PaymentStatus.AUTHORIZING, PaymentEvent.AUTHORIZE_SUCCESS), PaymentStatus.AUTHORIZED),
        Map.entry(new Transition(PaymentStatus.AUTHORIZING, PaymentEvent.AUTHORIZE_FAIL), PaymentStatus.FAILED),
        Map.entry(new Transition(PaymentStatus.AUTHORIZED, PaymentEvent.CAPTURE_REQUEST), PaymentStatus.CAPTURED),
        Map.entry(new Transition(PaymentStatus.CAPTURING, PaymentEvent.CAPTURE_SUCCESS), PaymentStatus.CAPTURED),
        Map.entry(new Transition(PaymentStatus.CAPTURING, PaymentEvent.CAPTURE_FAIL), PaymentStatus.AUTHORIZED),
        Map.entry(new Transition(PaymentStatus.CAPTURED, PaymentEvent.REFUND_INIT), PaymentStatus.PARTIAL_REFUNDING),
        Map.entry(new Transition(PaymentStatus.PARTIAL_REFUNDING, PaymentEvent.REFUND_COMPLETE), PaymentStatus.REFUNDING),
        Map.entry(new Transition(PaymentStatus.CAPTURED, PaymentEvent.REFUND_COMPLETE), PaymentStatus.REFUNDING),
        Map.entry(new Transition(PaymentStatus.CAPTURED, PaymentEvent.SETTLE), PaymentStatus.SETTLED),
        Map.entry(new Transition(PaymentStatus.SETTLED, PaymentEvent.REFUND_INIT), PaymentStatus.PARTIAL_REFUNDING),
        Map.entry(new Transition(PaymentStatus.CREATED, PaymentEvent.CANCEL), PaymentStatus.CANCELLED),
        Map.entry(new Transition(PaymentStatus.AUTHORIZING, PaymentEvent.CANCEL), PaymentStatus.CANCELLED),
        Map.entry(new Transition(PaymentStatus.AUTHORIZED, PaymentEvent.CAPTURE_TIMEOUT), PaymentStatus.AUTH_EXPIRED)
    );

    public PaymentStatus transition(PaymentStatus current, PaymentEvent event) {
        PaymentStatus next = TRANSITION.get(new Transition(current, event));
        if (next == null) {
            throw new InvalidStateTransitionException(current.name(), event.name());
        }
        return next;
    }
}
