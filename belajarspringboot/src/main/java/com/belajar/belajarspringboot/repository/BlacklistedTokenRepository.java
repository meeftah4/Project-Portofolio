package com.belajar.belajarspringboot.repository;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.belajar.belajarspringboot.model.BlacklistedToken;

@Repository
public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {
    boolean existsByTokenValueAndExpiresAtAfter(String tokenValue, Instant now);

    @Transactional
    long deleteByExpiresAtBefore(Instant instant);
}
