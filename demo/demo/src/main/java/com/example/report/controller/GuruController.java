//package com.example.report.controller;
//
//import com.example.report.model.Guru;
//import com.example.report.service.GuruService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/guru")
//public class GuruController {
//
//    private final GuruService guruService;
//
//    @Autowired
//    public GuruController(GuruService guruService) {
//        this.guruService = guruService;
//    }
//
//    @GetMapping("/all")
//    public List<Guru> getAllGurus() {
//        return guruService.getAllGurus();
//    }
//}
