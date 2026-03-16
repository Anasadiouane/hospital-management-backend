package com.hospital.hospital_management_backend.repository;

import com.hospital.hospital_management_backend.entity.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QRCodeRepository extends JpaRepository<QRCode, Long> {
}