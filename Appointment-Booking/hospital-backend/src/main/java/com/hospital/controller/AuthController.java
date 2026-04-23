package com.hospital.controller;

import com.hospital.dto.AuthRequest;
import com.hospital.dto.RegisterRequest;

import com.hospital.security.JwtUtil;
import com.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public java.util.Map<String, String> register(@RequestBody RegisterRequest request) {
        patientService.registerPatient(request);
        java.util.Map<String, String> response = new java.util.HashMap<>();
        response.put("message", "User registered successfully!");
        return response;
    }

    @PostMapping("/login")
    public com.hospital.dto.AuthResponse login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        if (authentication.isAuthenticated()) {
            return new com.hospital.dto.AuthResponse(jwtUtil.generateToken(request.getEmail()));
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }
}
