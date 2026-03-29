package com.belajar.belajarspringboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.belajar.belajarspringboot.model.PersonalInformation;

@Repository
public interface PersonalInformationRepository extends JpaRepository<PersonalInformation, Long> {
    Optional<PersonalInformation> findByCvId(Long cvId);
    @Transactional
    void deleteByCvId(Long cvId);
}
