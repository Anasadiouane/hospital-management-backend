package com.hospital.hospital_management_backend.repository;

import com.hospital.hospital_management_backend.entity.HospitalService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceRepository extends JpaRepository<HospitalService, Long> {
    Optional<HospitalService> findByName(String serviceName);
}