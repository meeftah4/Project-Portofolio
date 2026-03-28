package com.aicvbuilder.repository;

import com.aicvbuilder.entity.CV;
import com.aicvbuilder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CVRepository extends JpaRepository<CV, Long> {
    List<CV> findByUser(User user);
    Optional<CV> findByIdAndUser(Long id, User user);
    long countByUser(User user);
}
