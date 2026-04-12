package com.hospital.hospital_management_backend.repository;

import com.hospital.hospital_management_backend.dto.response.FileDtoResponse;
import com.hospital.hospital_management_backend.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByName(String fileName);

    @Query("SELECT new com.hospital.hospital_management_backend.dto.response.FileDtoResponse(" +
            "f.id, f.name, f.type, f.size) FROM File f")
    List<FileDtoResponse> findAllWithoutData();
}