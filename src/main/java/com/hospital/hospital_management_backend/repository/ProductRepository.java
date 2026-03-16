package com.hospital.hospital_management_backend.repository;


import com.hospital.hospital_management_backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}