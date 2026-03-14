package com.hospital.hospital_management_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serializable;

/**
 * DTO for updating stock quantity.
 * Validates that quantity is present and non-negative.
 */
public record StockUpdateDtoRequest(

        @NotNull(message = "Quantity is required.")
        @PositiveOrZero(message = "Quantity must be zero or greater.")
        Long quantity

) implements Serializable {}