package com.hospital.hospital_management_backend.repository;

import com.hospital.hospital_management_backend.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}