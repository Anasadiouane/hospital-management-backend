package com.hospital.hospital_management_backend.service.impl;

import com.hospital.hospital_management_backend.dto.request.MedicationDtoRequest;
import com.hospital.hospital_management_backend.dto.response.MedicationDtoResponse;
import com.hospital.hospital_management_backend.entity.Location;
import com.hospital.hospital_management_backend.entity.Medication;
import com.hospital.hospital_management_backend.entity.Stock;
import com.hospital.hospital_management_backend.exception.ResourceNotFoundException;
import com.hospital.hospital_management_backend.mapper.MedicationMapper;
import com.hospital.hospital_management_backend.repository.LocationRepository;
import com.hospital.hospital_management_backend.repository.MedicationRepository;
import com.hospital.hospital_management_backend.service.MedicationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service implementation for managing medications.
 */
@Service
public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepository medicationRepository;
    private final LocationRepository locationRepository;
    private final MedicationMapper medicationMapper;

    public MedicationServiceImpl(MedicationRepository medicationRepository,
                                 LocationRepository locationRepository,
                                 MedicationMapper medicationMapper) {
        this.medicationRepository = medicationRepository;
        this.locationRepository = locationRepository;
        this.medicationMapper = medicationMapper;
    }

    // =========================
    // CREATE
    // =========================
    @Transactional
    @Override
    public MedicationDtoResponse createMedication(MedicationDtoRequest request, Long locationId) {

        // resolve location
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location", "id", locationId.toString()));

        // build stock first (required by DB constraint)
        Stock stock = Stock.builder()
                .quantity(0L)
                .location(location)
                .build();

        // map and assign stock
        Medication medication = medicationMapper.toEntity(request);
        medication.setStock(stock);

        return medicationMapper.toDto(medicationRepository.save(medication));
    }

    // =========================
    // READ
    // =========================
    @Override
    @Transactional(readOnly = true)
    public MedicationDtoResponse getById(Long medicationId) {
        return medicationMapper.toDto(findMedication(medicationId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicationDtoResponse> getAllMedication() {
        return medicationRepository.findAll()
                .stream()
                .map(medicationMapper::toDto)
                .toList();
    }

    // =========================
    // UPDATE
    // =========================
    @Override
    @Transactional
    public MedicationDtoResponse updateMedication(Long medicationId, MedicationDtoRequest request) {
        Medication medication = findMedication(medicationId);
        medicationMapper.updateFromDto(request, medication);
        return medicationMapper.toDto(medicationRepository.save(medication));
    }

    // =========================
    // DELETE
    // =========================
    @Override
    @Transactional
    public void deleteMedication(Long medicationId) {
        medicationRepository.delete(findMedication(medicationId));
    }

    // =========================
    // HELPERS
    // =========================
    private Medication findMedication(Long id) {
        return medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medication", "id", id.toString()));
    }
}