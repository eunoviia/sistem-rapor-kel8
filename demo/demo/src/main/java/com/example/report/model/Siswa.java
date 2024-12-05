package com.example.report.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "siswa")
public class Siswa extends User {

    @Column(name = "siswa_id", nullable = false)
    private String siswaID;

    @Column(name = "siswa_nama", nullable = false)
    private String siswaNama;

    @OneToMany(mappedBy = "siswa")  // mappedBy mengacu pada properti siswa di dalam Rapor
    private List<Rapor> rapors;

    // Constructor tanpa parameter
    public Siswa() {}

    // Constructor dengan parameter
    public Siswa(String siswaID, String siswaNama) {
        this.siswaID = siswaID;
        this.siswaNama = siswaNama;
    }

    @Override
    public boolean login(String email, String password) {
        // Logika login khusus untuk siswa
        return email.endsWith("@siswa.com") && password.length() > 5;
    }

    // Getter dan Setter untuk siswaID
    public String getSiswaID() {
        return siswaID;
    }

    public void setSiswaID(String siswaID) {
        this.siswaID = siswaID;
    }

    // Getter dan Setter untuk siswaNama
    public String getSiswaNama() {
        return siswaNama;
    }

    public void setSiswaNama(String siswaNama) {
        this.siswaNama = siswaNama;
    }
}
