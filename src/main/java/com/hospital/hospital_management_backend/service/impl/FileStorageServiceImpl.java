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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing file storage.
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final FileRepository fileRepository;
    private final FileMapper fileMapper;

    public FileStorageServiceImpl(FileRepository fileRepository, FileMapper fileMapper) {
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
    }

    @Override
    public FileDtoResponse uploadFile(MultipartFile file) {
        try {
            File newFile = new File();

            String fileName = file.getOriginalFilename();
            if (fileName == null || !fileName.contains(".")) {
                throw new AppException(HttpStatus.BAD_REQUEST, "Invalid file name");
            }

            newFile.setName(fileName.substring(0, fileName.lastIndexOf('.')));
            newFile.setType(file.getContentType());
            newFile.setData(FileUtils.compress(file.getBytes()));

            File savedFile = fileRepository.save(newFile);

            return new FileDtoResponse(
                    savedFile.getId(),
                    savedFile.getName(),
                    savedFile.getType(),
                    savedFile.getData().length
            );
        } catch (IOException e) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Failed to upload file");
        }
    }

    @Override
    public File downloadFile(Long fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File", "id", fileId.toString()));

        file.setData(FileUtils.decompress(file.getData()));
        return file;
    }

    @Override
    public List<FileDtoResponse> getAllFiles() {
        return fileRepository.findAll().stream()
                .map(fileMapper::fileToFileDtoResponse)
                .collect(Collectors.toList());
    }
}