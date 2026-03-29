package com.belajar.belajarspringboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.belajar.belajarspringboot.model.SkillEntry;

@Repository
public interface SkillEntryRepository extends JpaRepository<SkillEntry, Long> {
    List<SkillEntry> findByCvIdOrderByIdAsc(Long cvId);
    Optional<SkillEntry> findByIdAndCvId(Long id, Long cvId);
    @Transactional
    void deleteByCvId(Long cvId);
}
