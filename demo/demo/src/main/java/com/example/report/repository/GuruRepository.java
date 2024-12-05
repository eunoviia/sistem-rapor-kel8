package com.example.report.repository;

import com.example.report.model.Guru;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuruRepository extends JpaRepository<Guru, Integer> {
    // Tambahkan metode query kustom jika diperlukan
    Optional<Guru> findByEmail(String email);

}
