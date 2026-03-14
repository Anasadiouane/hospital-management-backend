package com.hospital.hospital_management_backend.dto.request;

import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * DTO for individual delivery notes within a stock order and product order.
 * Validates product ID and quantity.
 */
public record DeliveryNoteDto(

        @NotNull(message = "Product ID is required.")
        @Positive(message = "Product ID must be a positive number.")
        Long productId,

        @NotNull(message = "Quantity is required.")
        @Positive(message = "Quantity should be greater than 0.")
        Long quantity

) implements Serializable {}