package com.hospital.config;

import com.hospital.model.Doctor;
import com.hospital.repository.DoctorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final DoctorRepository doctorRepository;
    private final com.hospital.repository.AppointmentRepository appointmentRepository;

    public DataSeeder(DoctorRepository doctorRepository,
            com.hospital.repository.AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        appointmentRepository.deleteAll(); // Clear appointments to avoid FK constraints
        doctorRepository.deleteAll(); // Clear existing data to remove duplicates

        List<Doctor> doctors = new java.util.ArrayList<>();
        doctors.add(new Doctor(null, "Dr. Gopal Krishna Raju", "Cardiologist (MBBS, MD)", "RECURRING: MONDAY,WEDNESDAY,FRIDAY | 09:00,10:00,11:00,14:00,15:00", 800.0, "https://ui-avatars.com/api/?name=Gopal+Krishna+Raju&background=random"));
        doctors.add(new Doctor(null, "Dr. Divya Sri Reddy", "Pediatrician (MD)", "RECURRING: TUESDAY,THURSDAY | 10:00,11:00,14:00,16:00", 600.0, "https://ui-avatars.com/api/?name=Divya+Sri+Reddy&background=random"));
        doctors.add(new Doctor(null, "Dr. Dharani", "Dermatologist (MBBS, DDVL)", "RECURRING: MONDAY,THURSDAY | 09:00,10:30,13:00", 700.0, "https://ui-avatars.com/api/?name=Dharani&background=random"));
        doctors.add(new Doctor(null, "Dr. Naveen Chandra Reddy", "Neurologist (DM)", "RECURRING: FRIDAY | 10:00,11:00,12:00,14:00,15:00,16:00", 1000.0, "https://ui-avatars.com/api/?name=Naveen+Chandra+Reddy&background=random"));
        doctors.add(new Doctor(null, "Dr. Surya Naidu", "Orthopedic Surgeon (MS)", "RECURRING: MONDAY,WEDNESDAY | 08:00,09:00,15:00,16:00", 900.0, "https://ui-avatars.com/api/?name=Surya+Naidu&background=random"));
        doctors.add(new Doctor(null, "Dr. Samyuktha Chowdary", "General Physician (MBBS)", "RECURRING: MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY | 09:00,12:00,17:00", 400.0, "https://ui-avatars.com/api/?name=Samyuktha+Chowdary&background=random"));
        doctors.add(new Doctor(null, "Dr. Siri", "Pulmonologist (MD)", "RECURRING: SATURDAY,SUNDAY | 10:00,12:00,14:00", 650.0, "https://ui-avatars.com/api/?name=Siri&background=random"));
        doctors.add(new Doctor(null, "Dr. Sukesh Singh", "Gynecologist (MBBS, MS)", "RECURRING: WEDNESDAY,SATURDAY | 10:00,12:00", 750.0, "https://ui-avatars.com/api/?name=Sukesh+Singh&background=random"));
        doctors.add(new Doctor(null, "Dr. Chandra Sekhar", "Yoga & Naturopathy Specialist(BNYS)", "RECURRING: TUESDAY,FRIDAY | 09:00,11:00,16:00", 550.0, "https://ui-avatars.com/api/?name=Chandra+Sekhar&background=random"));

        doctorRepository.saveAll(doctors);
        System.out.println("Database reset and seeded with " + doctors.size() + " unique doctors.");
    }
    
    // Helper method removed as it is no longer needed
    private String generateSlots(int daysPerMonth, int months, int startHour) {
        return ""; 
    }
}
