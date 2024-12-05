package com.example.report.service;

import com.example.report.model.*;
import com.example.report.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private GuruRepository guruRepository;

    @Autowired
    private SiswaRepository siswaRepository;

    // Register Admin
    public Guru registerGuru(String name, String email, String password) {
        Guru newAdmin = new Guru();
        newAdmin.setName(name);
        newAdmin.setEmail(email);
        newAdmin.setGuruNama(name);  // Set adminNama dengan nilai yang valid
        newAdmin.setPassword((password)); // Hash password
        newAdmin.setRole("GURU"); // Set default role
        newAdmin.setGuruID("GURU" + System.currentTimeMillis()); // Unique ID
        guruRepository.save(newAdmin);
        return newAdmin;
    }

    // Register Customer
    public Siswa registerCustomer(String name, String email, String password) {
        Siswa newCustomer = new Siswa();
        newCustomer.setName(name);
        newCustomer.setEmail(email);
        newCustomer.setSiswaNama(name);
        newCustomer.setPassword((password)); // Hash password
        newCustomer.setSiswaID("SIS" + System.currentTimeMillis()); // Generate unique customer ID
        newCustomer.setRole("SISWA"); // Set default role
        // Simpan logika hash password di sini jika diperlukan
        siswaRepository.save(newCustomer);
        return newCustomer;
    }

    // Login Admin
    public Optional<Guru> loginAdmin(String email, String password) {
        // Cari admin berdasarkan email
        Optional<Guru> adminOpt = guruRepository.findByEmail(email);

        if (adminOpt.isPresent()) {
            Guru admin = adminOpt.get();

            // Validasi email dan password
            if (admin.getEmail().equals(email) && admin.getPassword().equals(password)) {
                return Optional.of(admin);
            }
        }

        // Jika tidak valid, kembalikan Optional kosong
        return Optional.empty();
    }

    // Login Customer
    public Optional<Siswa> loginCustomer(String email, String password) {
        // Cari admin berdasarkan email
        Optional<Siswa> cust = siswaRepository.findByEmail(email);

        if (cust.isPresent()) {
            Siswa req = cust.get();

            // Validasi email dan password
            if (req.getEmail().equals(email) && req.getPassword().equals(password)) {
                return Optional.of(req);
            }
        }

        // Jika tidak valid, kembalikan Optional kosong
        return Optional.empty();
    }

    public List<Siswa> getAllStudents() {
        return siswaRepository.findAll();
    }

    // Metode untuk menghitung jumlah siswa yang terdaftar
    public long getTotalStudentCount() {
        return siswaRepository.count();
    }

    public long getTotalTeacherCount() {
        return guruRepository.count();
    }

    public Siswa getSiswaById(Long id) {
        return siswaRepository.findById(id).orElse(null);
    }
}
