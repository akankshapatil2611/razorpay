package com.gayeway.Razorpay.vault.service.impl;

import com.gayeway.Razorpay.common.entity.Money;
import com.gayeway.Razorpay.common.enums.CardBrand;
import com.gayeway.Razorpay.common.exception.ResourceNotFoundException;
import com.gayeway.Razorpay.common.util.RandomUtil;
import com.gayeway.Razorpay.payment.processor.PaymentProcessorRouter;
import com.gayeway.Razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.gayeway.Razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.gayeway.Razorpay.vault.config.VaultEncryptionConfig;
import com.gayeway.Razorpay.vault.dto.Request.TokenizeRequest;
import com.gayeway.Razorpay.vault.dto.Response.TokenizeResponse;
import com.gayeway.Razorpay.vault.entity.CardToken;
import com.gayeway.Razorpay.vault.entity.VaultCard;
import com.gayeway.Razorpay.vault.repository.CardTokenRepository;
import com.gayeway.Razorpay.vault.repository.VaultCardRepository;
import com.gayeway.Razorpay.vault.service.VaultService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class VaultServiceImpl implements VaultService {

    private final CardTokenRepository cardTokenRepository;
    private final VaultCardRepository vaultCardRepository;
    private final BytesEncryptor dekEncrypter;
    private final PaymentProcessorRouter paymentProcessorRouter;


    @Override
    @Transactional
    public TokenizeResponse tokenize(TokenizeRequest tokenizeRequest, UUID merchantId) {

        String lastFour = tokenizeRequest.pan().substring(tokenizeRequest.pan().length()-4);
        String bin = tokenizeRequest.pan().substring(0,6);
        CardBrand cardBrand = detectBrand(tokenizeRequest.pan());

        byte[] dek = KeyGenerators.secureRandom(32).generateKey();
        byte[] encryptedPan = VaultEncryptionConfig.panEncryptor(dek)
                .encrypt(tokenizeRequest.pan().getBytes(StandardCharsets.UTF_8));
        byte[] encryptdek = dekEncrypter.encrypt(dek);

        VaultCard vaultCard = vaultCardRepository.save(VaultCard.builder()
                .brand(cardBrand)
                .expiry_year(tokenizeRequest.expiryYear().toString())
                .expiry_month(tokenizeRequest.expiryMonth().toString())
                .bin(bin)
                .last_four(lastFour)
                .encryptedDek(encryptdek)
                .encryptedPan(encryptedPan)
                .cardHolderName(tokenizeRequest.cardHolderName())
                .build());

        String token = "tok_" + RandomUtil.randomBase64(32);

        cardTokenRepository.save(CardToken.builder()
                .vaultCard(vaultCard)
                .token(token)
                .customerId(tokenizeRequest.customerId())
                .merchantId(merchantId)
                .build());

        return new TokenizeResponse(token, lastFour, cardBrand, tokenizeRequest.expiryMonth(), tokenizeRequest.expiryYear());
    }

    @Override
    public PaymentProcessorResponse charge(UUID paymentId, String token, Money amount, Map<String, Object> methodDetails) {

        CardToken cardToken = cardTokenRepository.findByTokenAndRevokedAtIsNull(token)
                .orElseThrow(() -> new ResourceNotFoundException("CardToken", token));

        VaultCard vaultCard = cardToken.getVaultCard();
        byte[] panBytes = null;
        try{
            byte[] dek = dekEncrypter.decrypt(vaultCard.getEncryptedDek());
            panBytes = VaultEncryptionConfig.panEncryptor(dek).decrypt(vaultCard.getEncryptedPan());

            String pan = new String(panBytes, StandardCharsets.UTF_8);
            String expiry = vaultCard.getExpiry_month() + "/" + vaultCard.getExpiry_year();

            PaymentProcessorRequest paymentProcessorRequest = PaymentProcessorRequest
                .card(paymentId, pan, expiry, amount, methodDetails);

            PaymentProcessorResponse response = paymentProcessorRouter.charge(paymentProcessorRequest);

            log.info("Vault charge registered, token={}****", token.substring(0, 4));

            return response;
        } catch (Exception e) {
            log.warn("Vault charge failed, token={}****", token.substring(0, 4));
            return new PaymentProcessorResponse.failure("VAULT_CHARGE_FAILED", e.getMessage());
        } finally {
            if (panBytes != null) Arrays.fill(panBytes, (byte) 0);
        }
    }

    private CardBrand detectBrand(String pan) {
        if (pan.startsWith("4")) return CardBrand.VISA;
        if (pan.startsWith("5") || pan.startsWith("2")) return CardBrand.MASTERCARD;
        if (pan.startsWith("37") || pan.startsWith("34")) return CardBrand.AMEX;
        return CardBrand.RUPAY;
    }
}
