package com.hospital.hospital_management_backend.repository;

import com.hospital.hospital_management_backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByCode(Long code);
}