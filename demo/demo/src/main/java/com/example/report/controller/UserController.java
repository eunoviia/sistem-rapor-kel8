package com.example.report.controller;

import com.example.report.model.*;
import com.example.report.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Register Admin
    @PostMapping("/register/teacher")
    public ResponseEntity<?> registerAdmin(@RequestBody Map<String, Object> body, HttpSession session) {
        String name = (String) body.get("name");
        String email = (String) body.get("email");
        String password = (String) body.get("password");

        if (name == null || email == null || password == null || name.isBlank() || email.isBlank() || password.isBlank()) {
            return ResponseEntity.badRequest().body("Required parameters 'name', 'email', and 'password' must not be empty.");
        }

        Guru newAdmin = userService.registerGuru(name, email, password);

        // Simpan adminId ke dalam sesi setelah berhasil mendaftar
        session.setAttribute("adminId", newAdmin.getId());

//        return ResponseEntity.status(302) // Status redirect
//                .header("Location", "/login") // Header lokasi untuk pengalihan
//                .build();
        return ResponseEntity.ok(Map.of(
                "message", "Customer registration successful",
                "guru", newAdmin.getId()
        ));
    }

//    @GetMapping("/register")
//    public String showRegisterPage(Model model) {
//        model.addAttribute("user", new customer()); // Atau new Customer()
//        return "register"; // Mengacu pada src/main/resources/templates/register.html
//    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // Mengacu pada src/main/resources/templates/register.html
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Mengacu ke login.html di templates
    }

    // Register Customer
    @PostMapping("/register/student")
    public ResponseEntity<?> registerCustomer(@RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        String email = (String) body.get("email");
        String password = (String) body.get("password");

        if (name == null || email == null || password == null || name.isBlank() || email.isBlank() || password.isBlank()) {
            return ResponseEntity.badRequest().body("Required parameters 'name', 'email', and 'password' must not be empty.");
        }

        Siswa newCustomer = userService.registerCustomer(name, email, password);
//        return ResponseEntity.status(302) // Status redirect
////                .header("Location", "/login") // Header lokasi untuk pengalihan
////                .build();
        return ResponseEntity.ok(Map.of(
                "message", "Customer registration successful",
                "siswa", newCustomer.getId()
        ));
    }


    @PostMapping("/login/teacher")
    public ResponseEntity<Object> loginAdmin(@RequestBody Map<String, Object> body, HttpSession session) {
        if (body == null || !body.containsKey("email") || !body.containsKey("password")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing required fields: 'email' and 'password'"));
        }

        String email = (String) body.get("email");
        String password = (String) body.get("password");

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email and password must not be empty"));
        }

        // Proses login melalui service
        return userService.loginAdmin(email, password)
                .<ResponseEntity<Object>>map(admin -> {
                    // Simpan adminId ke dalam sesi
                    session.setAttribute("adminId", admin.getId());
                    // Login berhasil, kirimkan JSON
                    return ResponseEntity.ok((Object) Map.of(
                            "message", "Login Successful",
                            "adminId", admin.getId(),
                            "email", admin.getEmail(),
                            "name", admin.getName()
                    ));
                })
                .orElse(ResponseEntity.status(401).body(Map.of("error", "Invalid credentials")));
    }



    @PostMapping("/login/student")
    public ResponseEntity<Object> loginCustomer(@RequestBody Map<String, Object> body, HttpSession session) {
        // Validasi untuk memastikan data tidak null atau kosong
        if (body == null || !body.containsKey("email") || !body.containsKey("password")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing required fields: 'email' and 'password'"));
        }

        // Parsing dan validasi nilai dari request body
        String email = (String) body.get("email");
        String password = (String) body.get("password");

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email and password must not be empty"));
        }

        // Proses login melalui service
        return userService.loginCustomer(email, password)
                .<ResponseEntity<Object>>map(customer -> {
                    // Simpan customerId ke dalam sesi
                    session.setAttribute("customerId", customer.getId());
                    // Login berhasil, kirimkan JSON
                    return ResponseEntity.ok((Object) Map.of(
                            "message", "Login Successful",
                            "customerId", customer.getId(),
                            "email", customer.getEmail(),
                            "name", customer.getName()
                    ));
                })
                .orElse(ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"))); // Respons gagal
    }

    // Endpoint untuk mendapatkan semua data siswa
    @GetMapping("/students")
    public ResponseEntity<List<Siswa>> getAllStudents() {
        List<Siswa> students = userService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    // Endpoint untuk mendapatkan jumlah total siswa
    @GetMapping("/students/count")
    public ResponseEntity<Map<String, Object>> getStudentCount() {
        long totalStudents = userService.getTotalStudentCount();
        return ResponseEntity.ok(Map.of("totalStudents", totalStudents));
    }

    @GetMapping("/guru/count")
    public ResponseEntity<Map<String, Object>> getTeacherCount() {
        long totalStudents = userService.getTotalTeacherCount();
        return ResponseEntity.ok(Map.of("totalGuru", totalStudents));
    }
}


