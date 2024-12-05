package com.example.report.service;

import com.example.report.model.Rapor;
import com.example.report.model.Siswa;
import com.example.report.repository.RaporRepository;
import com.example.report.repository.SiswaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RaporService {

    @Autowired
    private RaporRepository raporRepository;

    @Autowired
    private SiswaRepository siswaRepository;

    public Rapor inputNilaiSiswa(Long siswaId, int matematika, int bahasaInggris, int biologi, int fisika, int kimia) {
        Optional<Siswa> siswaOpt = siswaRepository.findById(siswaId);

        if (siswaOpt.isEmpty()) {
            throw new IllegalArgumentException("Siswa dengan ID " + siswaId + " tidak ditemukan.");
        }

        Siswa siswa = siswaOpt.get();

        Rapor rapor = new Rapor();
        rapor.setRaporId("RAP" + System.currentTimeMillis()); // Generate unique ID
        rapor.setSiswa(siswa);
        rapor.setMatematika(matematika);
        rapor.setBahasaInggris(bahasaInggris);
        rapor.setBiologi(biologi);
        rapor.setFisika(fisika);
        rapor.setKimia(kimia);

        int point = 0;
        if (matematika >= 85) point += 5;
        if (bahasaInggris >= 85) point += 5;
        if (biologi >= 85) point += 5;
        if (fisika >= 85) point += 5;
        if (kimia >= 85) point += 5;

        rapor.setPoint(point);

        return raporRepository.save(rapor);
    }


    // Hapus rapor berdasarkan ID
    public void deleteRaporById(String raporId) {
        try {
            raporRepository.deleteById(raporId);
            System.out.println("Rapor berhasil dihapus: " + raporId);
        } catch (EmptyResultDataAccessException e) {
            System.err.println("Data rapor tidak ditemukan untuk ID: " + raporId);
            throw new RuntimeException("Data rapor tidak ditemukan untuk ID: " + raporId);
        }
    }


    // Ambil semua data rapor
    public List<Rapor> getAllRapor() {
        return raporRepository.findAll();
    }

    public long getTotalRaporCount() {
        return raporRepository.count();
    }

    public Rapor findRaporById(String id) {
        System.out.println("ID yang diterima: " + id); // Debug
        return raporRepository.findById(id).orElse(null);
    }

    public Rapor getRaporBySiswaId(Long siswaId) {
        // Cari rapor berdasarkan id_siswa
        return raporRepository.findBySiswaId(siswaId);
    }

    public Rapor updateRaporBySiswaId(Long siswaId, Rapor raporUpdate) {
        // Cari Rapor berdasarkan siswaId
        Rapor rapor = raporRepository.findBySiswaId(siswaId);
        if (rapor != null) {
            // Update nilai mata pelajaran
            rapor.setMatematika(raporUpdate.getMatematika());
            rapor.setBahasaInggris(raporUpdate.getBahasaInggris());
            rapor.setBiologi(raporUpdate.getBiologi());
            rapor.setFisika(raporUpdate.getFisika());
            rapor.setKimia(raporUpdate.getKimia());

            // Hitung ulang point berdasarkan nilai mata pelajaran
            int point = 0;
            if (rapor.getMatematika() >= 85) point += 5;
            if (rapor.getBahasaInggris() >= 85) point += 5;
            if (rapor.getBiologi() >= 85) point += 5;
            if (rapor.getFisika() >= 85) point += 5;
            if (rapor.getKimia() >= 85) point += 5;

            rapor.setPoint(point);

            // Simpan perubahan ke database
            return raporRepository.save(rapor);
        }
        return null;
    }



}
