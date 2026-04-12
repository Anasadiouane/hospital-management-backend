package com.hospital.hospital_management_backend.service;

import com.hospital.hospital_management_backend.dto.response.FileDtoResponse;
import com.hospital.hospital_management_backend.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {
    FileDtoResponse uploadFile(MultipartFile file);
    FileDtoResponse downloadFile(Long fileId);
    List<FileDtoResponse> getAllFiles();
}