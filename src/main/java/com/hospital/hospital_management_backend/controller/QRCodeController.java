package com.hospital.hospital_management_backend.controller;

import com.hospital.hospital_management_backend.dto.response.QRCodeDtoResponse;
import com.hospital.hospital_management_backend.service.QRCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/materials")
public class QRCodeController {

    private final QRCodeService qrCodeService;

    public QRCodeController(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    /** (Re)generate the QR code of a material. */
    @PostMapping("/{materialId}/qrcode")
    public ResponseEntity<QRCodeDtoResponse> generateQRCodeMaterial(@PathVariable Long materialId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(qrCodeService.generateQRCodeMaterial(materialId));
    }

    /** Get the QR code image (PNG) of a material. */
    @GetMapping(value = "/{materialId}/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQRCodeMaterial(@PathVariable Long materialId) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrCodeService.getQRCodeMaterial(materialId));
    }

    /** Upload a QR code image and get the decoded content. */
    @PostMapping(value = "/qrcode/read", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> readQRCodeMaterial(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(qrCodeService.readQRCodeMaterial(file.getBytes()));
    }
}
