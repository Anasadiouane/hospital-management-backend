package com.hospital.hospital_management_backend.service;

import com.hospital.hospital_management_backend.dto.request.MaterialDtoRequest;
import com.hospital.hospital_management_backend.dto.response.MaterialCreateDtoResponse;
import com.hospital.hospital_management_backend.dto.response.MaterialDtoResponse;

import java.util.List;

public interface MaterialService {
    MaterialCreateDtoResponse createMaterial(MaterialDtoRequest request, Long locationId);
    MaterialDtoResponse getById(Long materialId);
    List<MaterialDtoResponse> getAllMaterial();
    MaterialCreateDtoResponse updateMaterial(Long materialId, MaterialDtoRequest materialDtoRequest);
    void deleteMaterial(Long materialId);
}