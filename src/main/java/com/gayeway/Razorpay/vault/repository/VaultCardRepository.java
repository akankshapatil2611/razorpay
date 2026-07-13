package com.gayeway.Razorpay.vault.repository;

import com.gayeway.Razorpay.vault.entity.VaultCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VaultCardRepository extends JpaRepository<VaultCard, UUID> {
}