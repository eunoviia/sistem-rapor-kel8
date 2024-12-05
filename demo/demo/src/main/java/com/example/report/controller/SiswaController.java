//package com.example.report.controller;
//
//import com.example.report.model.Siswa;
//import com.example.report.service.SiswaService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/siswa")
//public class SiswaController {
//
//    @Autowired
//    private SiswaService siswaService;
//
//    @GetMapping
//    public ResponseEntity<List<Siswa>> findAll() {
//        return ResponseEntity.ok(siswaService.findAll());
//    }
//
//    @PostMapping
//    public ResponseEntity<Siswa> save(@RequestBody Siswa siswa) {
//        return ResponseEntity.ok(siswaService.save(siswa));
//    }
//
//    @DeleteMapping("/{nis}")
//    public ResponseEntity<Void> delete(@PathVariable String nis) {
//        siswaService.deleteById(nis);
//        return ResponseEntity.ok().build();
//    }
//}
