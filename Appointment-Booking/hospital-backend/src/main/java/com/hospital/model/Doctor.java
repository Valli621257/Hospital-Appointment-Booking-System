package com.hospital.model;

import jakarta.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String specialization;

    @Column(length = 10000)
    private String availability;

    @Column(name = "consultation_fee")
    private Double consultationFee = 500.0; // Default fee

    @Column(name = "image_url")
    private String imageUrl;

    public Doctor() {
    }

    public Doctor(Long doctorId, String name, String specialization, String availability, Double consultationFee, String imageUrl) {
        this.doctorId = doctorId;
        this.name = name;
        this.specialization = specialization;
        this.availability = availability;
        this.consultationFee = consultationFee != null ? consultationFee : 500.0;
        this.imageUrl = imageUrl;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public Double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(Double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
