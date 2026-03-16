package com.hospital.hospital_management_backend.repository;

import com.hospital.hospital_management_backend.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByName(String fileName);
}