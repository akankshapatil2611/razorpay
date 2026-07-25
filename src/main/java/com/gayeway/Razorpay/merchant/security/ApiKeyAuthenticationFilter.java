package com.gayeway.Razorpay.merchant.security;

import com.gayeway.Razorpay.merchant.cache.ApiKeyCache;
import com.gayeway.Razorpay.merchant.cache.ApiKeyCacheEntry;
import com.gayeway.Razorpay.merchant.entity.ApiKey;
import com.gayeway.Razorpay.merchant.repository.ApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private static final String BASIC_PREFIX = "Basic ";
    private final ApiKeyRepository apiKeyRepository;
    private final MerchantContext merchantContext;
    private final BCryptPasswordEncoder BCRYPT = new BCryptPasswordEncoder();
    private final HandlerExceptionResolver handlerExceptionResolver;

    private final ApiKeyCache apiKeyCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Incoming request: {}", request.getRequestURI());

        try {
            String header = request.getHeader("Authorization");
            if (header == null || !header.startsWith(BASIC_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }

            String[] credentials = decode(header);
            if(credentials == null){
                throw new BadRequestException("Malformed API Key Header");
            }
            String keyId = credentials[0];
            String rawSecret = credentials[1];

            ApiKeyCacheEntry apiKeyEntry = apiKeyCache.get(keyId)
                    .orElseGet(() -> loadAndCache(keyId));

//            ApiKey apiKey = apiKeyRepository.findByKeyId(keyId)
//                    .orElseThrow(() -> new BadRequestException("Invalid or missing API Key"));

            if (apiKeyEntry == null || !apiKeyEntry.enabled() || !secretMatches(rawSecret, apiKeyEntry)) {
                throw new BadRequestException("Invalid or missing API Key");
            }


            var auth = new UsernamePasswordAuthenticationToken(keyId, null,
                    List.of(new SimpleGrantedAuthority("API_KEY_ROLE"))
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
            merchantContext.setMerchantId(apiKeyEntry.merchantId());
            merchantContext.setKeyId(apiKeyEntry.keyId());

            filterChain.doFilter(request, response);

        }
        catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }

    private ApiKeyCacheEntry loadAndCache(String keyId) {
        ApiKey apiKey = apiKeyRepository.findByKeyId(keyId).orElse(null);
        if(apiKey == null) return null;
        ApiKeyCacheEntry apiKeyCacheEntry = new ApiKeyCacheEntry(
                apiKey.getKeyId(),
                apiKey.getKeySecretHash(),
                apiKey.getPreviousKeySecretHash(),
                apiKey.getGracePeriodExpiresAt(),
                apiKey.getMerchant().getId(),
                apiKey.getEnvironment(),
                apiKey.isEnabled()
        );
        apiKeyCache.put(keyId, apiKeyCacheEntry);
        return apiKeyCacheEntry;
    }

    private boolean secretMatches(String rawSecret, ApiKeyCacheEntry apiKey) {
        if (BCRYPT.matches(rawSecret, apiKey.keySecretHash())) {
            return true;
        }
        boolean isInGracePeriod = apiKey.gracePeriodExpiresAt() != null &&
                LocalDateTime.now().isBefore(apiKey.gracePeriodExpiresAt());
        return isInGracePeriod
                && apiKey.previousKeySecretHash() != null
                && BCRYPT.matches(rawSecret, apiKey.previousKeySecretHash());
    }

    public String[] decode(String header)
    {
        String encode = header.substring(BASIC_PREFIX.length());
        String decode = new String(Base64.getDecoder().decode(encode), StandardCharsets.UTF_8);

        int colon = decode.indexOf(":");
        if(colon < 1) return null;

        return new String[]{decode.substring(0, colon), decode.substring(colon+1)};
    }
}
