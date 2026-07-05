package com.gayeway.Razorpay.payment.repository;

import com.gayeway.Razorpay.payment.entity.PaymentTrasitionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentTransitionLogRepository extends JpaRepository<PaymentTrasitionLog, UUID> {


}
