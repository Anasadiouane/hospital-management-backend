package com.hospital.hospital_management_backend.controller;

import com.hospital.hospital_management_backend.dto.request.ProductOrderDtoRequest;
import com.hospital.hospital_management_backend.dto.response.ProductOrderDtoResponse;
import com.hospital.hospital_management_backend.service.ProductOrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productOrders")
public class ProductOrderController {

    private final ProductOrderService productOrderService;

    public ProductOrderController(ProductOrderService productOrderService) {
        this.productOrderService = productOrderService;
    }

    @PostMapping
    public ResponseEntity<ProductOrderDtoResponse> createProductOrder(
            @Valid @RequestBody ProductOrderDtoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productOrderService.createProductOrder(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductOrderDtoResponse> getProductOrder(@PathVariable Long id) {
        return ResponseEntity.ok(productOrderService.getProductOrder(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductOrderDtoResponse>> getAllProductOrder() {
        return ResponseEntity.ok(productOrderService.getAllProductOrder());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductOrderDtoResponse>> getProductOrdersOfProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productOrderService.getProductOrdersOfProduct(productId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductOrderDtoResponse> updateProductOrder(
            @PathVariable Long id,
            @Valid @RequestBody ProductOrderDtoRequest request) {
        return ResponseEntity.ok(productOrderService.updateProductOrder(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductOrder(@PathVariable Long id) {
        productOrderService.deleteProductOrder(id);
        return ResponseEntity.noContent().build();
    }
}
