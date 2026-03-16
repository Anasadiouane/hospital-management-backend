package com.hospital.hospital_management_backend.repository;

import com.hospital.hospital_management_backend.entity.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacationRepository extends JpaRepository<Vacation, Long> {
}