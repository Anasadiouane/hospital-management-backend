package com.hospital.hospital_management_backend.controller;

import com.hospital.hospital_management_backend.dto.request.StockDtoRequest;
import com.hospital.hospital_management_backend.dto.request.StockUpdateDtoRequest;
import com.hospital.hospital_management_backend.dto.response.StockDtoResponse;
import com.hospital.hospital_management_backend.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    public ResponseEntity<StockDtoResponse> createStock(@Valid @RequestBody StockDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockService.createStock(dto));
    }

    @GetMapping("/{stockId}")
    public ResponseEntity<StockDtoResponse> getById(@PathVariable Long stockId) {
        return ResponseEntity.ok(stockService.getById(stockId));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<StockDtoResponse> getStockByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(stockService.getStockByProduct(productId));
    }

    @GetMapping
    public ResponseEntity<List<StockDtoResponse>> getAllStock() {
        return ResponseEntity.ok(stockService.getAllStock());
    }

    @PutMapping("/{stockId}")
    public ResponseEntity<StockDtoResponse> updateStock(@PathVariable Long stockId,
                                                        @Valid @RequestBody StockUpdateDtoRequest dto) {
        return ResponseEntity.ok(stockService.updateStock(stockId, dto));
    }

    @PutMapping("/{stockId}/location/{locationId}")
    public ResponseEntity<StockDtoResponse> addLocationToStock(@PathVariable Long stockId,
                                                               @PathVariable Long locationId) {
        return ResponseEntity.ok(stockService.addLocationToStock(stockId, locationId));
    }

    @DeleteMapping("/{stockId}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long stockId) {
        stockService.deleteStock(stockId);
        return ResponseEntity.noContent().build();
    }
}
