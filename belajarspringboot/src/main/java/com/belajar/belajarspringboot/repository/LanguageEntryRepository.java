package com.belajar.belajarspringboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.belajar.belajarspringboot.model.LanguageEntry;

@Repository
public interface LanguageEntryRepository extends JpaRepository<LanguageEntry, Long> {
    List<LanguageEntry> findByCvIdOrderByIdAsc(Long cvId);
    Optional<LanguageEntry> findByIdAndCvId(Long id, Long cvId);
    @Transactional
    void deleteByCvId(Long cvId);
}
