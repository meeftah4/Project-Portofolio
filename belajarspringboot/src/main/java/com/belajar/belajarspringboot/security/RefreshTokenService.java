package com.belajar.belajarspringboot.security;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.belajar.belajarspringboot.model.RefreshToken;
import com.belajar.belajarspringboot.model.UserAccount;
import com.belajar.belajarspringboot.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final long expirationDays;
    private final SecureRandom secureRandom = new SecureRandom();

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            @Value("${app.security.refresh-token-expiration-days:14}") long expirationDays) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.expirationDays = expirationDays;
    }

    @Transactional
    public String issueRefreshToken(UserAccount account) {
        cleanupExpiredTokens();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserAccount(account);
        refreshToken.setToken(generateTokenValue());
        refreshToken.setExpiresAt(Instant.now().plus(expirationDays, ChronoUnit.DAYS));
        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken).getToken();
    }

    @Transactional
    public RotationResult rotateRefreshToken(String tokenValue) {
        RefreshToken current = getValidTokenOrThrow(tokenValue);
        UserAccount account = current.getUserAccount();

        current.setRevoked(true);
        refreshTokenRepository.save(current);

        String newRefreshToken = issueRefreshToken(account);
        return new RotationResult(account, newRefreshToken);
    }

    @Transactional
    public void revokeRefreshToken(String tokenValue) {
        if (tokenValue == null || tokenValue.isBlank()) {
            return;
        }

        refreshTokenRepository.findByToken(tokenValue.trim())
                .ifPresent(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });
    }

    private RefreshToken getValidTokenOrThrow(String tokenValue) {
        if (tokenValue == null || tokenValue.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token wajib diisi");
        }

        cleanupExpiredTokens();

        RefreshToken token = refreshTokenRepository.findByToken(tokenValue.trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token tidak valid"));

        if (token.isRevoked() || token.getExpiresAt().isBefore(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token tidak valid");
        }

        return token;
    }

    private void cleanupExpiredTokens() {
        refreshTokenRepository.deleteByExpiresAtBefore(Instant.now());
    }

    private String generateTokenValue() {
        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public record RotationResult(UserAccount userAccount, String refreshToken) {
    }
}
