package com.hospital.service;

import com.hospital.model.Doctor;
import com.hospital.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private com.hospital.repository.AppointmentRepository appointmentRepository;

    public Doctor addDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    public void deleteDoctor(Long id) {
        List<com.hospital.model.Appointment> appointments = appointmentRepository.findByDoctor_DoctorId(id);
        
        boolean hasActiveAppointments = appointments.stream()
                .anyMatch(a -> a.getStatus() != com.hospital.model.Appointment.AppointmentStatus.CANCELLED);

        if (hasActiveAppointments) {
            throw new RuntimeException("Cannot delete doctor. There are active appointments.");
        }

        // Delete cancelled appointments to clean up FK references
        appointmentRepository.deleteAll(appointments);
        doctorRepository.deleteById(id);
    }
}
