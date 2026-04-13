package com.hospital.hospital_management_backend.service;

import com.hospital.hospital_management_backend.dto.response.QRCodeDtoResponse;

public interface QRCodeService {
    QRCodeDtoResponse generateQRCodeMaterial(Long materialId);
    byte[] getQRCodeMaterial(Long materialId);
    String readQRCodeMaterial(byte[] qrCodeImage);
}