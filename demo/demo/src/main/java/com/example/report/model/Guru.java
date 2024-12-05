package com.example.report.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "guru")
public class Guru extends User {

    @Override
    public boolean login(String email, String password) {
        // Logika login khusus untuk admin
        return email.equals("guru@example.com") && password.equals("admin123");
    }

    @Column(name = "GuruID", nullable = false)
    private String guruID;

    @Column(nullable = false)  // Pastikan properti ini tidak boleh null
    private String guruNama;

    // Constructor
    public Guru() {}

    public Guru(String guruID, String guruNama) {
        this.guruID = guruID;
        this.guruNama = guruNama;
    }

    // Getter dan Setter
    public String getGuruID() {
        return guruID;
    }

    public void setGuruID(String guruID) {
        this.guruID = guruID;
    }

    public String getGuruNama() {
        return guruNama;
    }

    public void setGuruNama(String guruNama) {
        this.guruNama = guruNama;
    }
}
