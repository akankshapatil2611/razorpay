package com.gayeway.Razorpay.payment.repository;

import com.gayeway.Razorpay.payment.entity.OrderRecord;
import com.gayeway.Razorpay.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByOrder_Id(OrderRecord order);
}
