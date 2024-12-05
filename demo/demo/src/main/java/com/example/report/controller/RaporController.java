package com.example.report.controller;

import com.example.report.model.Rapor;
import com.example.report.service.RaporService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rapor")
public class RaporController {

    @Autowired
    private RaporService raporService;

    // Endpoint untuk input nilai siswa
    @PostMapping("/input")
    public ResponseEntity<?> inputNilaiSiswa(@RequestBody Map<String, Object> body) {
        try {
            Long siswaId = (Long) body.get("siswaId");
            int matematika = (int) body.get("matematika");
            int bahasaInggris = (int) body.get("bahasaInggris");
            int biologi = (int) body.get("biologi");
            int fisika = (int) body.get("fisika");
            int kimia = (int) body.get("kimia");

            Rapor rapor = raporService.inputNilaiSiswa(siswaId, matematika, bahasaInggris, biologi, fisika, kimia);
            return ResponseEntity.ok(Map.of(
                    "message", "Rapor berhasil disimpan",
                    "raporId", rapor.getRaporId()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Terjadi kesalahan saat menyimpan rapor"
            ));
        }
    }

//    @PutMapping("/edit/{id}")
//    public ResponseEntity<Object> editNilaiRapor(@PathVariable String id, @RequestBody Map<String, Object> nilaiBaru) {
//        try {
//            boolean isUpdated = raporService.updateNilaiRapor(id, nilaiBaru);
//            if (isUpdated) {
//                return ResponseEntity.ok(Map.of("message", "Rapor updated successfully"));
//            }
//            return ResponseEntity.status(404).body(Map.of("error", "Rapor not found"));
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(Map.of("error", "An error occurred while updating rapor"));
//        }
//    }

    @GetMapping("/{siswaId}")
    public ResponseEntity<Rapor> getRaporBySiswaId(@PathVariable Long siswaId) {
        Rapor rapor = raporService.getRaporBySiswaId(siswaId);
        if (rapor != null) {
            return ResponseEntity.ok(rapor);
        } else {
            return ResponseEntity.notFound().build();  // 404 jika rapor tidak ditemukan
        }
    }

    // Mengupdate nilai rapor
    @PutMapping("/update/{siswaId}")
    public Rapor updateRapor(@PathVariable Long siswaId, @RequestBody Rapor raporUpdate) {
        return raporService.updateRaporBySiswaId(siswaId, raporUpdate);
    }


    @DeleteMapping("/deleteRapor/{id}")
    public ResponseEntity<Map<String, Object>> deleteRapor(@PathVariable String id) {
        System.out.println("Permintaan DELETE untuk Rapor ID: " + id); // Debug
        try {
            raporService.deleteRaporById(id); // Tidak perlu memeriksa boolean
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Rapor berhasil dihapus."
            ));
        } catch (Exception e) {
            System.err.println("Error saat menghapus rapor: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                    "status", "error",
                    "message", "Terjadi kesalahan saat menghapus rapor: " + e.getMessage()
            ));
        }
    }



    @GetMapping("/all")
    public ResponseEntity<Object> getAllRapor() {
        try {
            List<Rapor> raporList = raporService.getAllRapor();
            return ResponseEntity.ok(raporList);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "An error occurred while retrieving rapor data"));
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getRaporCount() {
        long totalStudents = raporService.getTotalRaporCount();
        return ResponseEntity.ok(Map.of("totalRapor", totalStudents));
    }
}
