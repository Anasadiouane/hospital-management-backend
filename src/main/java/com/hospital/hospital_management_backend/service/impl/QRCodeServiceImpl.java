package com.hospital.hospital_management_backend.service.impl;

import com.hospital.hospital_management_backend.dto.response.QRCodeDtoResponse;
import com.hospital.hospital_management_backend.entity.Material;
import com.hospital.hospital_management_backend.entity.QRCode;
import com.hospital.hospital_management_backend.entity.Stock;
import com.hospital.hospital_management_backend.exception.AppException;
import com.hospital.hospital_management_backend.exception.ResourceNotFoundException;
import com.hospital.hospital_management_backend.repository.MaterialRepository;
import com.hospital.hospital_management_backend.repository.QRCodeRepository;
import com.hospital.hospital_management_backend.service.QRCodeService;
import com.hospital.hospital_management_backend.util.Constants;
import com.hospital.hospital_management_backend.util.QRCodeUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QRCodeServiceImpl implements QRCodeService {

    private final MaterialRepository materialRepository;
    private final QRCodeRepository qrCodeRepository;

    public QRCodeServiceImpl(MaterialRepository materialRepository,
                             QRCodeRepository qrCodeRepository) {
        this.materialRepository = materialRepository;
        this.qrCodeRepository = qrCodeRepository;
    }

    @Override
    @Transactional
    public QRCodeDtoResponse generateQRCodeMaterial(Long materialId) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new ResourceNotFoundException("Material", "id", materialId.toString()));

        // If QR code already exists, remove it
        if (material.getQrCode() != null) {
            QRCode qrCode = material.getQrCode();
            material.setQrCode(null);
            materialRepository.save(material);
            qrCodeRepository.delete(qrCode);
        }

        String qrCodeData = getString(material);

        QRCode qrCode = new QRCode();
        byte[] qrCodeImage = QRCodeUtils.generateQRCode(qrCodeData, Constants.QR_CODE_WIDTH, Constants.QR_CODE_HEIGHT);
        qrCode.setQrCodeImage(qrCodeImage);

        QRCode savedQRCode = qrCodeRepository.save(qrCode);

        material.setQrCode(savedQRCode);
        materialRepository.save(material);

        return new QRCodeDtoResponse(savedQRCode.getId(), savedQRCode.getQrCodeImage());
    }

    private static String getString(Material material) {
        Stock stock = material.getStock();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonObject = objectMapper.createObjectNode();
        jsonObject.put("id", material.getId());
        jsonObject.put("name", material.getName());
        jsonObject.put("description", material.getDescription());

        if (stock != null) {
            jsonObject.put("quantity", stock.getQuantity());
            if (stock.getLocation() != null) {
                jsonObject.put("location", stock.getLocation().getName());
            }
        }

        return jsonObject.toString();
    }

    @Override
    @Transactional
    public byte[] getQRCodeMaterial(Long materialId) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new ResourceNotFoundException("Material", "id", materialId.toString()));

        if (material.getQrCode() == null) {
            generateQRCodeMaterial(materialId);
        }

        return material.getQrCode().getQrCodeImage();
    }

    @Override
    public String readQRCodeMaterial(byte[] qrCodeImage) {
        String data = QRCodeUtils.readQRCode(qrCodeImage);

        // Adjust validation to match actual JSON payload
        if (!data.contains("id") || !data.contains("name")) {
            throw new AppException(HttpStatus.NOT_FOUND, "Invalid QR code image");
        }

        return data;
    }
}