package com.example.report.repository;

import com.example.report.model.Rapor;
import com.example.report.model.Siswa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RaporRepository extends JpaRepository<Rapor, String> {
    Rapor findBySiswaId(Long siswaId);  // Menemukan rapor berdasarkan id_siswa (foreign key)

    // Contoh query kustom
//    List<Rapor> findBySiswa_Nis(String nis); // Mencari rapor berdasarkan NIS
}
