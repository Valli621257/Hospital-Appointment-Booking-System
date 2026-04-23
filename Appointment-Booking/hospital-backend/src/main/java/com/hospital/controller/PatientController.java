package com.hospital.controller;

import com.hospital.model.Doctor;
import com.hospital.model.Patient;
import com.hospital.service.DoctorService;
import com.hospital.service.PatientService;
import com.hospital.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://localhost:5173")
public class PatientController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/doctors")
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("/profile")
    public Patient getProfile(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsername(token.substring(7));
        return patientService.getPatientByEmail(email);
    }
}
