package com.belajar.belajarspringboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.belajar.belajarspringboot.model.Education;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
    List<Education> findByCvIdOrderByIdAsc(Long cvId);
    Optional<Education> findByIdAndCvId(Long id, Long cvId);
    @Transactional
    void deleteByCvId(Long cvId);
}
