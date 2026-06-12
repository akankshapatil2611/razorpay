package com.gayeway.Razorpay.vault.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vault_card")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VaultCard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 4)
    private String last_four;

    @Column(nullable = false, length = 6)   // first 6 digit of card
    private String bin;

    // Pan number in encrypted format
    @Column(nullable = false)
    private byte[] encryptedPan;

    // String which we are using to encrypt pan - random String  - master key encrypt dek and then dek encrypt pan
    @Column(nullable = false)
    private byte[] encryptedDek;

    @Column(nullable = false)
    private String brand;       // VISA, Rupay

    @Column(nullable = false)
    private String expiry_month;

    @Column(nullable = false)
    private String expiry_year;

    @Column(nullable = false)
    private String cardHolderName;

    private LocalDateTime deleted_at;

}
