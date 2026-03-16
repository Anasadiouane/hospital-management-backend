package com.hospital.hospital_management_backend.repository;

import com.hospital.hospital_management_backend.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
}