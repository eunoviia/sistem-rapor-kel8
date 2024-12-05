package com.example.report.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "rapor")
public class Rapor {

    @Id
    @Column(length = 20)
    private String raporId;

    @ManyToOne
    @JoinColumn(name = "siswa_id", nullable = false) // Foreign key
    private Siswa siswa;

    @Column(nullable = false)
    private int point;

    @Column(nullable = false)
    private int matematika;

    @Column(nullable = false)
    private int bahasaInggris;

    @Column(nullable = false)
    private int biologi;

    @Column(nullable = false)
    private int fisika;

    @Column(nullable = false)
    private int kimia;
}
