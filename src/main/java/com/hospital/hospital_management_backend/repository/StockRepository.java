package com.hospital.hospital_management_backend.repository;

import com.hospital.hospital_management_backend.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}