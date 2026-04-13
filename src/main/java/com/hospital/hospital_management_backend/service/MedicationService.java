package com.hospital.hospital_management_backend.service;

import com.hospital.hospital_management_backend.dto.request.MedicationDtoRequest;
import com.hospital.hospital_management_backend.dto.response.MedicationDtoResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MedicationService {

    MedicationDtoResponse createMedication(MedicationDtoRequest request, Long locationId);

    MedicationDtoResponse getById(Long medicationId);

    List<MedicationDtoResponse> getAllMedication();

    MedicationDtoResponse updateMedication(Long medicationId, MedicationDtoRequest request);

    void deleteMedication(Long medicationId);
}