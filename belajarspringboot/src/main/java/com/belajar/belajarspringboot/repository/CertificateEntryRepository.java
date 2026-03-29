package com.belajar.belajarspringboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.belajar.belajarspringboot.model.CertificateEntry;

@Repository
public interface CertificateEntryRepository extends JpaRepository<CertificateEntry, Long> {
    List<CertificateEntry> findByCvIdOrderByIdAsc(Long cvId);
    Optional<CertificateEntry> findByIdAndCvId(Long id, Long cvId);
    @Transactional
    void deleteByCvId(Long cvId);
}
