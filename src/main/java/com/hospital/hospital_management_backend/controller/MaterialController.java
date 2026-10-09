package com.hospital.hospital_management_backend.controller;

import com.hospital.hospital_management_backend.dto.request.MaterialDtoRequest;
import com.hospital.hospital_management_backend.dto.response.MaterialCreateDtoResponse;
import com.hospital.hospital_management_backend.dto.response.MaterialDtoResponse;
import com.hospital.hospital_management_backend.service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping("/location/{locationId}")
    public ResponseEntity<MaterialCreateDtoResponse> createMaterial(@PathVariable Long locationId,
                                                                    @Valid @RequestBody MaterialDtoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(materialService.createMaterial(request, locationId));
    }

    @GetMapping("/{materialId}")
    public ResponseEntity<MaterialDtoResponse> getById(@PathVariable Long materialId) {
        return ResponseEntity.ok(materialService.getById(materialId));
    }

    @GetMapping
    public ResponseEntity<List<MaterialDtoResponse>> getAllMaterial() {
        return ResponseEntity.ok(materialService.getAllMaterial());
    }

    @PutMapping("/{materialId}")
    public ResponseEntity<MaterialCreateDtoResponse> updateMaterial(@PathVariable Long materialId,
                                                                    @Valid @RequestBody MaterialDtoRequest dto) {
        return ResponseEntity.ok(materialService.updateMaterial(materialId, dto));
    }

    @DeleteMapping("/{materialId}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long materialId) {
        materialService.deleteMaterial(materialId);
        return ResponseEntity.noContent().build();
    }
}
