package com.hospital.hospital_management_backend.controller;

import com.hospital.hospital_management_backend.dto.request.MedicationDtoRequest;
import com.hospital.hospital_management_backend.dto.response.MedicationDtoResponse;
import com.hospital.hospital_management_backend.service.MedicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medications")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @PostMapping("/location/{locationId}")
    public ResponseEntity<MedicationDtoResponse> createMedication(@PathVariable Long locationId,
                                                                  @Valid @RequestBody MedicationDtoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(medicationService.createMedication(request, locationId));
    }

    @GetMapping("/{medicationId}")
    public ResponseEntity<MedicationDtoResponse> getById(@PathVariable Long medicationId) {
        return ResponseEntity.ok(medicationService.getById(medicationId));
    }

    @GetMapping
    public ResponseEntity<List<MedicationDtoResponse>> getAllMedication() {
        return ResponseEntity.ok(medicationService.getAllMedication());
    }

    @PutMapping("/{medicationId}")
    public ResponseEntity<MedicationDtoResponse> updateMedication(@PathVariable Long medicationId,
                                                                  @Valid @RequestBody MedicationDtoRequest request) {
        return ResponseEntity.ok(medicationService.updateMedication(medicationId, request));
    }

    @DeleteMapping("/{medicationId}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long medicationId) {
        medicationService.deleteMedication(medicationId);
        return ResponseEntity.noContent().build();
    }
}
