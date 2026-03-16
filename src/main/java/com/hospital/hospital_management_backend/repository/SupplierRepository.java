package com.hospital.hospital_management_backend.repository;

import com.hospital.hospital_management_backend.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}