package com.example.report.controller;

import com.example.report.model.Guru;
import com.example.report.model.Rapor;
import com.example.report.model.Siswa;
import com.example.report.service.RaporService;
import com.example.report.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    private final UserService userService;
    private final RaporService raporService;
    private final String RAPOR_API_URL = "http://localhost:8080/api/rapor/all"; // URL API rapor

    public DashboardController(UserService userService, RaporService raporService) {
        this.userService = userService;
        this.raporService = raporService;
    }

    // Untuk halaman HTML
    // Untuk halaman HTML
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        // Cek jika guru belum login
        if (session.getAttribute("adminId") == null) {
            return "redirect:/login/teacher"; // Arahkan ke halaman login guru
        }

        long totalStudents = userService.getTotalStudentCount();
        long totalRapor = raporService.getTotalRaporCount();
        long totalGuru = userService.getTotalTeacherCount();

        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("totalRapor", totalRapor);
        model.addAttribute("totalGuru", totalGuru);

        return "dashboard_guru"; // Hanya bisa diakses oleh guru
    }


    // Untuk API JSON
    @GetMapping("/students/count")
    @ResponseBody
    public Map<String, Object> getStudentCount() {
        long totalStudents = userService.getTotalStudentCount();
        return Map.of("totalStudents", totalStudents);
    }

    @GetMapping("/guru/count")
    @ResponseBody
    public Map<String, Object> getTeacherCount() {
        long totalGuru = userService.getTotalTeacherCount();
        return Map.of("totalGuru", totalGuru);
    }

    @GetMapping("/rapor/count")
    @ResponseBody
    public Map<String, Object> getRaporCount() {
        long totalRapor = raporService.getTotalRaporCount();
        return Map.of("totalRapor", totalRapor);
    }

    @GetMapping("/data-siswa")
    public String dataSiswaPage(Model model) {
        // Ambil daftar siswa dari service
        List<Siswa> students = userService.getAllStudents();

        // Tambahkan data siswa ke model
        model.addAttribute("students", students);

        // Return nama file HTML tanpa ekstensi
        return "data_siswa";
    }

    @GetMapping("/data-rapor")
    public String dataRaporPage(Model model) {
        List<Rapor> raporList = raporService.getAllRapor();
        raporList.forEach(rapor -> System.out.println("Rapor ID: " + rapor.getRaporId()));
        model.addAttribute("raporList", raporList);
        return "data_rapor";
    }


    @GetMapping("/login/teacher")
    public String showLoginPage() {
        return "login_guru"; // Nama file HTML untuk login
    }

    @PostMapping("/login/teacher")
    public String loginTeacher(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        if (username.isBlank() || password.isBlank()) {
            model.addAttribute("error", "Email dan password harus diisi.");
            return "login_guru";
        }

        return userService.loginAdmin(username, password)
                .map(admin -> {
                    // Simpan adminId di sesi
                    session.setAttribute("adminId", admin.getId());
                    session.setAttribute("adminName", admin.getName());
                    session.setAttribute("adminEmail", admin.getEmail());

                    // Redirect ke dashboard jika berhasil login
                    return "redirect:/dashboard";
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Email atau password salah.");
                    return "login_guru"; // Kembali ke halaman login jika gagal
                });
    }

    @GetMapping("/login-siswa")
    public String showLoginSiswaPage() {
        // Mengarahkan ke halaman login siswa
        return "login_siswa";
    }



    @PostMapping("/login-siswa")
    public String loginSiswa(
            @RequestParam("username") String email,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {
        // Validasi input form
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            model.addAttribute("error", "Email dan password tidak boleh kosong.");
            return "login_siswa";
        }

        // Memproses login menggunakan UserService
        return userService.loginCustomer(email, password)
                .map(customer -> {
                    // Simpan informasi user di session
                    session.setAttribute("siswaId", customer.getId());
                    session.setAttribute("siswaName", customer.getName());

                    // Redirect ke dashboard siswa
                    return "redirect:/dashboard-siswa";
                })
                .orElseGet(() -> {
                    // Jika login gagal, tampilkan pesan error
                    model.addAttribute("error", "Email atau password salah.");
                    return "login_siswa";
                });
    }

    @GetMapping("/dashboard-siswa")
    public String showDashboardSiswa(HttpSession session, Model model) {
        // Ambil data dari session
        Object siswaName = session.getAttribute("siswaName");
        Object siswaId = session.getAttribute("siswaId");

        // Jika session kosong, redirect ke halaman login
        if (siswaName == null || siswaId == null) {
            return "redirect:/login-siswa";
        }

        // Ambil detail siswa dari database menggunakan siswaId
        Siswa siswa = userService.getSiswaById((Long) siswaId);
        if (siswa == null) {
            return "redirect:/login-siswa"; // Jika data siswa tidak ditemukan, redirect ke login
        }

        // Tambahkan data ke model untuk dikirim ke view
        model.addAttribute("siswaName", siswa.getName());
        model.addAttribute("siswaId", siswa.getSiswaID());
        model.addAttribute("email", siswa.getEmail());
        model.addAttribute("password", siswa.getPassword()); // Password mungkin perlu dienkripsi atau disembunyikan

        return "dashboard_siswa"; // Nama file HTML untuk dashboard
    }

    @GetMapping("/nilai-siswa")
    public String showDashboardSiswaNilai(HttpSession session, Model model) {
        Long siswaId = (Long) session.getAttribute("siswaId");

        if (siswaId == null) {
            return "redirect:/login-siswa"; // Redirect to login if not logged in
        }

        Siswa siswa = userService.getSiswaById(siswaId);
        if (siswa == null) {
            return "redirect:/login-siswa"; // Redirect to login if siswa not found
        }

        Rapor rapor = raporService.getRaporBySiswaId(siswaId);

        model.addAttribute("siswaName", siswa.getName());
        model.addAttribute("email", siswa.getEmail());
        model.addAttribute("siswaId", siswa.getSiswaID());

        if (rapor == null) {
            // Jika rapor kosong, set isRaporEmpty ke true
            model.addAttribute("isRaporEmpty", true);
        } else {
            // Jika rapor ada, set isRaporEmpty ke false dan tambahkan data rapor
            model.addAttribute("isRaporEmpty", false);
            model.addAttribute("rapor", rapor);
        }

        return "nilai_siswa";
    }

    @GetMapping("/register/teacher")
    public String showRegisterTeacherForm() {
        return "register_guru"; // Nama file HTML tanpa ekstensi
    }

    @PostMapping("/register/teacher")
    public String registerTeacher(
            @RequestParam("full_name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model,
            HttpSession session) {

        // Validasi input
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            model.addAttribute("error", "All fields are required!");
            return "register_guru"; // Kembali ke form jika validasi gagal
        }

        try {
            // Panggil service untuk mendaftarkan guru
            Guru newGuru = userService.registerGuru(name, email, password);

            // Simpan adminId ke sesi setelah berhasil mendaftar
            session.setAttribute("guruId", newGuru.getId());

            // Redirect ke halaman login setelah sukses
            return "redirect:/login/teacher";
        } catch (Exception e) {
            // Tambahkan error ke model jika ada masalah
            model.addAttribute("error", "Failed to register teacher: " + e.getMessage());
            return "register_guru"; // Kembali ke form jika gagal
        }
    }

    @GetMapping("/register/siswa")
    public String showRegisterSiswaForm() {
        return "register_siswa"; // Nama file HTML tanpa ekstensi
    }

    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        String email = (String) body.get("email");
        String password = (String) body.get("password");

        if (name == null || email == null || password == null || name.isBlank() || email.isBlank() || password.isBlank()) {
            return ResponseEntity.badRequest().body("Required parameters 'name', 'email', and 'password' must not be empty.");
        }

        try {
            // Panggil service untuk mendaftarkan siswa
            Siswa newSiswa = userService.registerCustomer(name, email, password);

            // Return response sukses dengan ID siswa yang baru terdaftar
            return ResponseEntity.ok(Map.of(
                    "message", "Student registration successful",
                    "siswa", newSiswa.getId()
            ));
        } catch (Exception e) {
            // Handle error jika terjadi masalah pada proses pendaftaran
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to register student: " + e.getMessage());
        }
    }



    @GetMapping("/form-input-nilai/{id}")
    public String showInputNilaiForm(@PathVariable Long id, Model model) {
        Siswa siswa = userService.getSiswaById(id);
        model.addAttribute("siswa", siswa);
        return "form_input_nilai"; // View form_input_nilai.html
    }

    @PostMapping("/input-nilai")
    public String inputNilaiSiswa(
            @RequestParam Long siswaId,
            @RequestParam int matematika,
            @RequestParam int fisika,
            @RequestParam int kimia,
            @RequestParam int bahasaIndonesia,
            @RequestParam int bahasaInggris,
            Model model) {
        try {
            // Panggil service untuk menyimpan data
            raporService.inputNilaiSiswa(siswaId, matematika, fisika, kimia, bahasaIndonesia, bahasaInggris);
            model.addAttribute("message", "Nilai berhasil disimpan.");
        } catch (Exception e) {
            model.addAttribute("error", "Terjadi kesalahan: " + e.getMessage());
        }
        return "redirect:/data-siswa";
    }

    @GetMapping("rapor/edit-nilai/{siswaId}")
    public String showEditForm(@PathVariable Long siswaId, Model model) {
        // Ambil rapor berdasarkan siswaId
        Rapor rapor = raporService.getRaporBySiswaId(siswaId);
        if (rapor != null) {
            model.addAttribute("rapor", rapor);  // Menambahkan objek Rapor ke model
            model.addAttribute("siswa", rapor.getSiswa());  // Menambahkan data siswa ke model
            return "form_edit_nilai";  // Return ke halaman form edit nilai
        }
        return "redirect:/";  // Kalau rapor tidak ditemukan, redirect ke halaman utama
    }

    // Menangani pengiriman form edit nilai
    @PostMapping("/edit-nilai/update/{siswaId}")
    public String updateRapor(@PathVariable Long siswaId, @ModelAttribute Rapor raporUpdate, Model model) {
        Rapor updatedRapor = raporService.updateRaporBySiswaId(siswaId, raporUpdate);
        if (updatedRapor != null) {
            model.addAttribute("rapor", updatedRapor);
            return "redirect:/data-siswa";  // Redirect ke halaman detail siswa setelah update
        }
        return "error";  // Jika gagal update
    }



    @DeleteMapping("/deleteRapor/{id}")
    public ResponseEntity<Map<String, Object>> deleteRapor(@PathVariable String id) {
        System.out.println("Permintaan DELETE untuk Rapor ID: " + id); // Debug log
        try {
            raporService.deleteRaporById(id);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Rapor berhasil dihapus."
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "status", "error",
                    "message", "Gagal menghapus rapor: " + e.getMessage()
            ));
        }
    }





}
