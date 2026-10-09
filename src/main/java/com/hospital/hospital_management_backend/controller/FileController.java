package com.hospital.hospital_management_backend.controller;

import com.hospital.hospital_management_backend.dto.response.FileDtoResponse;
import com.hospital.hospital_management_backend.service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileDtoResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fileStorageService.uploadFile(file));
    }

    /**
     * NOTE: your service's downloadFile() currently returns only metadata
     * (id, name, type, size), not the file bytes. See the notes in the reply.
     */
    @GetMapping("/{fileId}")
    public ResponseEntity<FileDtoResponse> downloadFile(@PathVariable Long fileId) {
        return ResponseEntity.ok(fileStorageService.downloadFile(fileId));
    }

    @GetMapping
    public ResponseEntity<List<FileDtoResponse>> getAllFiles() {
        return ResponseEntity.ok(fileStorageService.getAllFiles());
    }
}
