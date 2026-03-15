package com.hospital.hospital_management_backend.dto.response;

import java.io.Serializable;


public record QRCodeDtoResponse(Long id, byte[] qrCodeImage) implements Serializable {
}