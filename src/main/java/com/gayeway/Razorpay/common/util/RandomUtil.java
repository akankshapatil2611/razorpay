package com.gayeway.Razorpay.common.util;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomUtil {

    // secure random is thread safe - random number generates always unique
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String randomBase64(int length)
    {
        byte[] buf = new byte[length];
        SECURE_RANDOM.nextBytes(buf);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
    }
}
