package com.hospital.hospital_management_backend.controller;

import com.hospital.hospital_management_backend.dto.request.DiplomaDtoRequest;
import com.hospital.hospital_management_backend.dto.response.DiplomaDtoResponse;
import com.hospital.hospital_management_backend.service.DiplomaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class DiplomaController {

    private final DiplomaService diplomaService;

    public DiplomaController(DiplomaService diplomaService) {
        this.diplomaService = diplomaService;
    }

    @PostMapping("/employees/{employeeId}/diplomas")
    public ResponseEntity<DiplomaDtoResponse> createDiploma(@PathVariable Long employeeId,
                                                            @Valid @RequestBody DiplomaDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(diplomaService.createDiploma(employeeId, dto));
    }

    @GetMapping("/employees/{employeeId}/diplomas/{diplomaId}")
    public ResponseEntity<DiplomaDtoResponse> getDiplomaById(@PathVariable Long employeeId,
                                                             @PathVariable Long diplomaId) {
        return ResponseEntity.ok(diplomaService.getDiplomaById(employeeId, diplomaId));
    }

    @GetMapping("/employees/{employeeId}/diplomas")
    public ResponseEntity<List<DiplomaDtoResponse>> getEmployeeDiplomas(@PathVariable Long employeeId) {
        return ResponseEntity.ok(diplomaService.getEmployeeDiplomas(employeeId));
    }

    @GetMapping("/diplomas")
    public ResponseEntity<List<DiplomaDtoResponse>> getAllDiploma() {
        return ResponseEntity.ok(diplomaService.getAllDiploma());
    }

    @PutMapping("/employees/{employeeId}/diplomas/{diplomaId}")
    public ResponseEntity<DiplomaDtoResponse> updateDiploma(@PathVariable Long employeeId,
                                                            @PathVariable Long diplomaId,
                                                            @Valid @RequestBody DiplomaDtoRequest dto) {
        return ResponseEntity.ok(diplomaService.updateDiploma(employeeId, diplomaId, dto));
    }

    @PutMapping("/employees/{employeeId}/diplomas/{diplomaId}/document/{documentId}")
    public ResponseEntity<DiplomaDtoResponse> addDocumentToDiploma(@PathVariable Long employeeId,
                                                                   @PathVariable Long diplomaId,
                                                                   @PathVariable Long documentId) {
        return ResponseEntity.ok(diplomaService.addDocumentToDiploma(employeeId, diplomaId, documentId));
    }

    @DeleteMapping("/employees/{employeeId}/diplomas/{diplomaId}")
    public ResponseEntity<Void> deleteDiploma(@PathVariable Long employeeId,
                                              @PathVariable Long diplomaId) {
        diplomaService.deleteDiploma(employeeId, diplomaId);
        return ResponseEntity.noContent().build();
    }
}
