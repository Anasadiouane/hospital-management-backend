package com.hospital.hospital_management_backend.repository;

import com.hospital.hospital_management_backend.entity.Diploma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiplomaRepository extends JpaRepository<Diploma, Long> {
    List<Diploma> findByEmployeeId(Long employeeId);
}