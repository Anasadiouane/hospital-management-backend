package com.hospital.hospital_management_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;

/**
 * DTO for stock creation or update requests.
 * Validates product reference and quantity.
 */
public record StockDtoRequest(

        @NotNull(message = "Product ID is required.")
        @Positive(message = "Product ID must be a positive number.")
        Long productId,

        @NotNull(message = "Quantity is required.")
        @Positive(message = "Quantity must be a positive number.")
        Long quantity

) implements Serializable {}