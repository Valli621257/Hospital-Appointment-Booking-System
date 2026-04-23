package com.hospital.repository;

import com.hospital.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.model.Doctor;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatient_PatientId(Long patientId);

    List<Appointment> findByDoctor_DoctorId(Long doctorId);

    long countByDoctorAndAppointmentDateAndStatusNot(Doctor doctor, LocalDateTime appointmentDate, Appointment.AppointmentStatus status);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Appointment a WHERE a.patient.patientId = :patientId AND a.doctor.doctorId = :doctorId AND a.appointmentDate = :appointmentDate AND a.status <> :status")
    boolean checkDuplicateBooking(@Param("patientId") Long patientId, @Param("doctorId") Long doctorId, @Param("appointmentDate") LocalDateTime appointmentDate, @Param("status") Appointment.AppointmentStatus status);
}
