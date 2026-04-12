package com.hospital.hospital_management_backend.service.impl;

import com.hospital.hospital_management_backend.dto.response.FileDtoResponse;
import com.hospital.hospital_management_backend.entity.File;
import com.hospital.hospital_management_backend.exception.AppException;
import com.hospital.hospital_management_backend.exception.ResourceNotFoundException;
import com.hospital.hospital_management_backend.mapper.FileMapper;
import com.hospital.hospital_management_backend.repository.FileRepository;
import com.hospital.hospital_management_backend.service.FileStorageService;
import com.hospital.hospital_management_backend.util.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Service implementation for managing file storage.
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final FileRepository fileRepository;
    private final FileMapper fileMapper;

    public FileStorageServiceImpl(FileRepository fileRepository,
                                  FileMapper fileMapper) {
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
    }

    @Override
    @Transactional
    public FileDtoResponse uploadFile(MultipartFile file) {
        try {
            validateFile(file);
            File entity = buildFileEntity(file);
            return fileMapper.toDto(fileRepository.save(entity));
        } catch (IOException e) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Failed to upload file");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public FileDtoResponse downloadFile(Long fileId) {
        File file = findFile(fileId);
        return new FileDtoResponse(file.getId(), file.getName(), file.getType(), file.getSize());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileDtoResponse> getAllFiles() {
        return fileRepository.findAllWithoutData();
    }

    private File findFile(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File", "id", id.toString()));
    }

    private void validateFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.contains(".")) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Invalid file name");
        }
    }

    private File buildFileEntity(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Invalid file name");
        }
        String name = fileName.substring(0, fileName.lastIndexOf('.'));
        return File.builder()
                .name(name)
                .type(file.getContentType())
                .data(FileUtils.compress(file.getBytes()))
                .size(file.getSize())
                .build();
    }
}