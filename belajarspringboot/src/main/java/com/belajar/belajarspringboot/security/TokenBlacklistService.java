package com.belajar.belajarspringboot.security;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.belajar.belajarspringboot.model.BlacklistedToken;
import com.belajar.belajarspringboot.repository.BlacklistedTokenRepository;

@Service
public class TokenBlacklistService {

    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final JwtService jwtService;

    public TokenBlacklistService(BlacklistedTokenRepository blacklistedTokenRepository, JwtService jwtService) {
        this.blacklistedTokenRepository = blacklistedTokenRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public void blacklistAccessToken(String tokenValue) {
        if (tokenValue == null || tokenValue.isBlank()) {
            return;
        }

        cleanupExpiredTokens();

        Instant now = Instant.now();
        Instant expiresAt;
        try {
            expiresAt = jwtService.extractExpiration(tokenValue);
        } catch (RuntimeException ex) {
            return;
        }

        if (expiresAt.isBefore(now)) {
            return;
        }

        if (blacklistedTokenRepository.existsByTokenValueAndExpiresAtAfter(tokenValue, now)) {
            return;
        }

        BlacklistedToken token = new BlacklistedToken();
        token.setTokenValue(tokenValue);
        token.setExpiresAt(expiresAt);
        blacklistedTokenRepository.save(token);
    }

    public boolean isBlacklisted(String tokenValue) {
        if (tokenValue == null || tokenValue.isBlank()) {
            return false;
        }

        cleanupExpiredTokens();
        return blacklistedTokenRepository.existsByTokenValueAndExpiresAtAfter(tokenValue, Instant.now());
    }

    private void cleanupExpiredTokens() {
        blacklistedTokenRepository.deleteByExpiresAtBefore(Instant.now());
    }
}
