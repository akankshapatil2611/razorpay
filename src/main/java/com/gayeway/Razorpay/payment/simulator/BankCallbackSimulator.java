package com.gayeway.Razorpay.payment.simulator;

import com.gayeway.Razorpay.common.enums.ChaosMode;
import com.gayeway.Razorpay.common.enums.PaymentStatus;
import com.gayeway.Razorpay.common.util.RandomUtil;
import com.gayeway.Razorpay.payment.entity.Payment;
import com.gayeway.Razorpay.payment.repository.PaymentRepository;
import com.gayeway.Razorpay.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class BankCallbackSimulator {

    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;
    private final SimulatorConfig simulatorConfig;

    @Scheduled(fixedDelayString = "${payment.simulator.poll-interval-ms:5000}")
    public void processCallbacks(){

        LocalDateTime globalTime = LocalDateTime.now().minusSeconds(1);
        List<Payment> candidates = paymentRepository.findByStatusAndCreatedAtBefore(PaymentStatus.AUTHORIZING, globalTime);

        for(Payment payment : candidates){
            simulateCallback(payment);
        }
    }

    private void simulateCallback(Payment payment) {

        SimulatorConfig.MethodSimulatorConfig methodSimulatorConfig = simulatorConfig.configFor(payment.getMethod());

        LocalDateTime dueAt = dueAt(methodSimulatorConfig, payment);

        if(LocalDateTime.now().isBefore(dueAt)) {
            return;
        }

        ChaosMode chaosMode = simulatorConfig.getChaosMode();

        switch (chaosMode) {
            case SUCCESS -> resolve(payment, true);
            case FAILURE -> resolve(payment, false);
            case TIMEOUT -> {
                log.debug("BankCallback simulator: Payment Timed out");
            }
            case NORMAL, SLOW -> resolve(payment, shouldApprove(payment, methodSimulatorConfig));
        }

    }

    private void resolve(Payment payment, boolean approve) {
        if(approve){
            String bankRef = "SIM_BANK_REF"+ RandomUtil.randomBase64(8);
            paymentService.resolveAuthorization(payment.getId(), true, bankRef, null, null);
        }
        else {
            paymentService.resolveAuthorization(payment.getId(), false, null, "SIM_BANK_ERROR_CODE", "Simulated Bank Decline");
        }
    }

    private boolean shouldApprove(Payment payment, SimulatorConfig.MethodSimulatorConfig methodConfig) {
        int bucket = Math.abs(payment.getId().hashCode()) % 100;
        return bucket < methodConfig.getSuccessRate();
    }


    private LocalDateTime dueAt(SimulatorConfig.MethodSimulatorConfig methodSimulatorConfig, Payment payment) {

        int range = methodSimulatorConfig.getMaxDelaySeconds() - methodSimulatorConfig.getMinDelaySeconds();
        int delaySeconds = methodSimulatorConfig.getMinDelaySeconds() + Math.abs(payment.getId().hashCode()) % (range+1);

        if(simulatorConfig.getChaosMode() == ChaosMode.SLOW){
            delaySeconds *= 2;
        }
        return payment.getCreatedAt().plusSeconds(delaySeconds);
    }
}
