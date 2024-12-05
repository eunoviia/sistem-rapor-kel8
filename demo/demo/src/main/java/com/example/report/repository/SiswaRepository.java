package com.example.report.repository;

import com.example.report.model.Siswa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SiswaRepository extends JpaRepository<Siswa, Long> {
    Optional<Siswa> findByEmail(String email);

}
