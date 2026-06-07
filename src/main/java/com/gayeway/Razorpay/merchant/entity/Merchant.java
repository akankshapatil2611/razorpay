package com.gayeway.Razorpay.merchant.entity;

import com.gayeway.Razorpay.common.enums.BusinessType;
import com.gayeway.Razorpay.common.enums.MerchantStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "merchant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    @Column(length = 10)
    private String contactNumber;

    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private BusinessType businessType;

    @Column(length = 100)
    private String businessName;

    @Column(length = 200)
    private String websiteUrl;

    @Column(length = 100, nullable = false)
    @Enumerated(EnumType.STRING)
    private MerchantStatus merchantStatus;

    @Column(length = 20)
    private String gstId;

    @Column(length = 20)
    private String panId;

    @Column(length = 100)
    private String settlementBankAccount;

    @Column(length = 20)
    private String settlementBankIfscCode;

    @Column(length = 100)
    private String settlmentBankAccountHolderName;

}
