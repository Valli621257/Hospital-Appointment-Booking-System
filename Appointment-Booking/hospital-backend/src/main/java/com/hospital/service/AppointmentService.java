package com.hospital.service;

import com.hospital.model.Appointment;
import com.hospital.model.Doctor;
import com.hospital.model.Patient;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public Appointment bookAppointment(Long patientId, Long doctorId, LocalDateTime date, String paymentMethod) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // 1. Check if THIS patient has already booked this slot
        if (appointmentRepository.checkDuplicateBooking(patientId, doctorId, date, Appointment.AppointmentStatus.CANCELLED)) {
             throw new RuntimeException("You have already booked an appointment for this time.");
        }

        // 2. Check for total slot availability (Max 2 bookings per slot)
        long activeBookings = appointmentRepository.countByDoctorAndAppointmentDateAndStatusNot(
                doctor, date, Appointment.AppointmentStatus.CANCELLED);
        
        System.out.println("DEBUG: Checking slots for Doctor=" + doctorId + ", Date=" + date);
        System.out.println("DEBUG: Active Bookings found: " + activeBookings);

        if (activeBookings >= 2) {
            System.out.println("DEBUG: Slot FULL. Rejecting booking.");
            throw new RuntimeException("Slot is full. Maximum 2 bookings allowed for this time.");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(date);
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setStatus(Appointment.AppointmentStatus.BOOKED);
        appointment.setFee(doctor.getConsultationFee()); // Set the fee at booking time
        
        appointment.setPaymentMethod(paymentMethod);
        if ("Cash".equalsIgnoreCase(paymentMethod)) {
            appointment.setAmountPaid(100.0); // Advance payment for Cash
        } else {
            appointment.setAmountPaid(doctor.getConsultationFee()); // Full payment for Online
        }
        
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatient_PatientId(patientId);
    }

    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctor_DoctorId(doctorId);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        
        if (appointment.getCreatedAt() != null) {
            long minutesSinceBooking = ChronoUnit.MINUTES.between(appointment.getCreatedAt(), LocalDateTime.now());
            if (minutesSinceBooking > 10) {
                throw new RuntimeException("Cancellation is only allowed within 10 minutes of booking");
            }
        } else {
             // For legacy appointments without createdAt, we assume they are older than 10 mins
             throw new RuntimeException("Cannot cancel historical appointments");
        }

        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);

        // Refund Logic: Refund only if NOT Cash
        if (!"Cash".equalsIgnoreCase(appointment.getPaymentMethod()) && appointment.getAmountPaid() != null) {
            // Deduct ₹100 cancellation fee
            double refund = Math.max(0, appointment.getAmountPaid() - 100.0);
            appointment.setRefundAmount(refund);
            System.out.println("DEBUG: Processed refund of ₹" + refund + " (Paid: " + appointment.getAmountPaid() + " - 100 cancellation fee) for Appointment ID: " + appointmentId);
        } else {
            appointment.setRefundAmount(0.0);
            System.out.println("DEBUG: No refund for Cash payment or unpaid appointment ID: " + appointmentId);
        }

        return appointmentRepository.save(appointment);
    }
}
