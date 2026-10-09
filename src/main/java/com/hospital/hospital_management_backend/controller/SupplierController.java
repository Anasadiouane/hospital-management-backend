package com.hospital.hospital_management_backend.controller;

import com.hospital.hospital_management_backend.dto.request.SupplierDtoRequest;
import com.hospital.hospital_management_backend.dto.response.StockOrderDtoResponse;
import com.hospital.hospital_management_backend.dto.response.SupplierDtoResponse;
import com.hospital.hospital_management_backend.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping
    public ResponseEntity<SupplierDtoResponse> createSupplier(@Valid @RequestBody SupplierDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.createSupplier(dto));
    }

    @GetMapping("/{supplierId}")
    public ResponseEntity<SupplierDtoResponse> getById(@PathVariable Long supplierId) {
        return ResponseEntity.ok(supplierService.getById(supplierId));
    }

    @GetMapping
    public ResponseEntity<List<SupplierDtoResponse>> getAllSupplier() {
        return ResponseEntity.ok(supplierService.getAllSupplier());
    }

    @GetMapping("/{supplierId}/stock-orders")
    public ResponseEntity<List<StockOrderDtoResponse>> getStockOrdersForSupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(supplierService.getStockOrdersForSupplier(supplierId));
    }

    @PutMapping("/{supplierId}")
    public ResponseEntity<SupplierDtoResponse> updateSupplier(@PathVariable Long supplierId,
                                                              @Valid @RequestBody SupplierDtoRequest dto) {
        return ResponseEntity.ok(supplierService.updateSupplier(supplierId, dto));
    }

    @DeleteMapping("/{supplierId}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long supplierId) {
        supplierService.deleteSupplier(supplierId);
        return ResponseEntity.noContent().build();
    }
}
