//package com.example.report.service;
//
//import com.example.report.model.Guru;
//import com.example.report.repository.GuruRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.util.List;
//
//@Service
//public class GuruService {
//
//    private final GuruRepository guruRepository;
//
//    @Autowired
//    public GuruService(GuruRepository guruRepository) {
//        this.guruRepository = guruRepository;
//    }
//
//    public Guru save(Guru guru) {
//        return guruRepository.save(guru);
//    }
//
//    public List<Guru> getAllGurus() {
//        return guruRepository.findAll();
//    }
//}
