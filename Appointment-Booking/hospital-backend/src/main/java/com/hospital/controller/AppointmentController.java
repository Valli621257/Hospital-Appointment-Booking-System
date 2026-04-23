package com.hospital.controller;

import com.hospital.model.Appointment;
import com.hospital.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@RequestBody Map<String, Object> payload) {
        try {
            System.out.println("Booking Payload: " + payload);
        Long patientId = Long.valueOf(payload.get("patientId").toString());
        Long doctorId = Long.valueOf(payload.get("doctorId").toString());
        String paymentMethod = payload.getOrDefault("paymentMethod", "Unknown").toString();
        
        String dateStr = payload.get("date").toString();
        System.out.println("Parsing Date: " + dateStr);
        
        // Ensure proper ISO format parsing
        LocalDateTime date;
        try {
            // Fix: Replace space with T to make it ISO-8601 compatible if needed
            if (dateStr.contains(" ")) {
                dateStr = dateStr.replace(" ", "T");
            }
            // Try standard ISO
            date = LocalDateTime.parse(dateStr);
        } catch (Exception e) {
             try {
                // Try appending seconds if missing (though frontend should handle it)
                date = LocalDateTime.parse(dateStr + ":00");
             } catch (Exception e2) {
                 System.out.println("Date Parse Error: " + e.getMessage());
                 throw new RuntimeException("Invalid date format: " + dateStr);
             }
        }

            Appointment appointment = appointmentService.bookAppointment(patientId, doctorId, date, paymentMethod);
            return ResponseEntity.ok(appointment);
            
        } catch (Exception e) {
            System.out.println("Error processing booking: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(java.util.Collections.singletonMap("message", e.getMessage()));
        }
    }

    @GetMapping("/patient/{patientId}")
    public List<Appointment> getPatientAppointments(@PathVariable Long patientId) {
        return appointmentService.getAppointmentsByPatient(patientId);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> getDoctorAppointments(@PathVariable Long doctorId) {
        return appointmentService.getAppointmentsByDoctor(doctorId);
    }

    @GetMapping("/all")
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @PutMapping("/{id}/cancel")
    public Appointment cancelAppointment(@PathVariable Long id) {
        return appointmentService.cancelAppointment(id);
    }
}
