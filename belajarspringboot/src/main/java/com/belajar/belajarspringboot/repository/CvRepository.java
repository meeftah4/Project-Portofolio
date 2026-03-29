package com.belajar.belajarspringboot.repository;

import com.belajar.belajarspringboot.model.Cv;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CvRepository extends JpaRepository<Cv, Long> {
	Optional<Cv> findByIdAndOwnerId(Long id, Long ownerId);
	List<Cv> findByOwnerIdOrderByIdAsc(Long ownerId);
	long countByOwnerId(Long ownerId);
	long countByOwnerIsNull();
}
