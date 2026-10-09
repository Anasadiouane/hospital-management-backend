package com.hospital.hospital_management_backend.controller;

import com.hospital.hospital_management_backend.dto.request.StockOrderDtoRequest;
import com.hospital.hospital_management_backend.dto.response.StockOrderDtoResponse;
import com.hospital.hospital_management_backend.service.StockOrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stockOrders")
public class StockOrderController {

    private final StockOrderService stockOrderService;

    public StockOrderController(StockOrderService stockOrderService) {
        this.stockOrderService = stockOrderService;
    }

    @PostMapping
    public ResponseEntity<StockOrderDtoResponse> createStockOrder(
            @Valid @RequestBody StockOrderDtoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockOrderService.createStockOrder(request));
    }

    @GetMapping("/{code}")
    public ResponseEntity<StockOrderDtoResponse> getStockOrder(@PathVariable Long code) {
        return ResponseEntity.ok(stockOrderService.getStockOrder(code));
    }

    @GetMapping
    public ResponseEntity<List<StockOrderDtoResponse>> getAllStockOrder() {
        return ResponseEntity.ok(stockOrderService.getAllStockOrder());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<StockOrderDtoResponse>> getStockOrdersOfProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(stockOrderService.getStockOrdersOfProduct(productId));
    }

    @PutMapping("/{code}")
    public ResponseEntity<StockOrderDtoResponse> updateStockOrder(
            @PathVariable Long code,
            @Valid @RequestBody StockOrderDtoRequest request) {
        return ResponseEntity.ok(stockOrderService.updateStockOrder(code, request));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteStockOrder(@PathVariable Long code) {
        stockOrderService.deleteStockOrder(code);
        return ResponseEntity.noContent().build();
    }
}
