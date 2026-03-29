package com.belajar.belajarspringboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.belajar.belajarspringboot.model.ProjectEntry;

@Repository
public interface ProjectEntryRepository extends JpaRepository<ProjectEntry, Long> {
    List<ProjectEntry> findByCvIdOrderByIdAsc(Long cvId);
    Optional<ProjectEntry> findByIdAndCvId(Long id, Long cvId);
    @Transactional
    void deleteByCvId(Long cvId);
}
