package com.belajar.belajarspringboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.belajar.belajarspringboot.model.WorkExperience;

@Repository
public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long> {
    List<WorkExperience> findByCvIdOrderByIdAsc(Long cvId);
    Optional<WorkExperience> findByIdAndCvId(Long id, Long cvId);
    @Transactional
    void deleteByCvId(Long cvId);
}
