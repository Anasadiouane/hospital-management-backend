package com.hospital.hospital_management_backend.repository;

import com.hospital.hospital_management_backend.entity.HospitalService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceRepository extends JpaRepository<HospitalService, Long> {
    Optional<HospitalService> findByName(String serviceName);

    boolean existsByName(@NotBlank(message = "Service name is required.") @Size(min = 3, max = 50, message = "Service name must be between {min} and {max} characters.") String name);
}