package com.hospital.hospital_management_backend.service.impl;

import com.hospital.hospital_management_backend.dto.request.MaterialDtoRequest;
import com.hospital.hospital_management_backend.dto.response.MaterialCreateDtoResponse;
import com.hospital.hospital_management_backend.dto.response.MaterialDtoResponse;
import com.hospital.hospital_management_backend.dto.response.QRCodeDtoResponse;
import com.hospital.hospital_management_backend.entity.Location;
import com.hospital.hospital_management_backend.entity.Material;
import com.hospital.hospital_management_backend.entity.QRCode;
import com.hospital.hospital_management_backend.entity.Stock;
import com.hospital.hospital_management_backend.exception.ResourceNotFoundException;
import com.hospital.hospital_management_backend.mapper.MaterialMapper;
import com.hospital.hospital_management_backend.repository.LocationRepository;
import com.hospital.hospital_management_backend.repository.MaterialRepository;
import com.hospital.hospital_management_backend.repository.QRCodeRepository;
import com.hospital.hospital_management_backend.service.MaterialService;
import com.hospital.hospital_management_backend.service.QRCodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service implementation for managing materials.
 */
@Service
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;
    private final LocationRepository locationRepository;
    private final QRCodeService qrCodeService;
    private final QRCodeRepository qrCodeRepository;
    private final MaterialMapper materialMapper;

    public MaterialServiceImpl(MaterialRepository materialRepository,
                               LocationRepository locationRepository,
                               QRCodeService qrCodeService,
                               QRCodeRepository qrCodeRepository,
                               MaterialMapper materialMapper) {
        this.materialRepository = materialRepository;
        this.locationRepository = locationRepository;
        this.qrCodeService = qrCodeService;
        this.qrCodeRepository = qrCodeRepository;
        this.materialMapper = materialMapper;
    }

    // =========================
    // CREATE
    // =========================
    @Override
    @Transactional
    public MaterialCreateDtoResponse createMaterial(MaterialDtoRequest request, Long locationId) {

        // resolve location
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location", "id", locationId.toString()));

        // build stock first (required by DB constraint)
        Stock stock = Stock.builder()
                .quantity(0L)
                .location(location)
                .build();

        // map material and assign stock
        Material material = materialMapper.toEntity(request);
        material.setStock(stock);

        // save to generate ID
        material = materialRepository.save(material);

        // generate and attach QR code
        QRCodeDtoResponse qrCodeDto = qrCodeService.generateQRCodeMaterial(material.getId());
        QRCode qrCode = qrCodeRepository.findById(qrCodeDto.id())
                .orElseThrow(() -> new ResourceNotFoundException("QRCode", "id", qrCodeDto.id().toString()));

        material.setQrCode(qrCode);

        return materialMapper.toCreateDto(materialRepository.save(material));
    }

    // =========================
    // READ
    // =========================
    @Override
    @Transactional(readOnly = true)
    public MaterialDtoResponse getById(Long materialId) {
        return materialMapper.toDto(findMaterial(materialId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaterialDtoResponse> getAllMaterial() {
        return materialRepository.findAll()
                .stream()
                .map(materialMapper::toDto)
                .toList();
    }

    // =========================
    // UPDATE
    // =========================
    @Override
    @Transactional
    public MaterialCreateDtoResponse updateMaterial(Long materialId, MaterialDtoRequest dto) {
        Material material = findMaterial(materialId);
        materialMapper.updateMaterialFromDto(dto, material);
        return materialMapper.toCreateDto(materialRepository.save(material));
    }

    // =========================
    // DELETE
    // =========================
    @Override
    @Transactional
    public void deleteMaterial(Long materialId) {
        materialRepository.delete(findMaterial(materialId));
    }

    // =========================
    // HELPERS
    // =========================
    private Material findMaterial(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material", "id", id.toString()));
    }
}