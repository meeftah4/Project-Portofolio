package com.belajar.belajarspringboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.belajar.belajarspringboot.model.Pengguna;

@Repository
public interface PenggunaRepository extends JpaRepository<Pengguna, Long> {
    
}
