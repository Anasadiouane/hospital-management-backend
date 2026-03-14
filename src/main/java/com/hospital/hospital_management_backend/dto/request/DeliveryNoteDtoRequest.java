package com.hospital.hospital_management_backend.dto.request;

import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * DTO for delivery note creation requests.
 * Validates order details and quantity constraints.
 */
public record DeliveryNoteDtoRequest(

        @NotBlank(message = "Order code should not be blank.")
        String orderCode,

        @NotNull(message = "Product ID is required.")
        Long productId,

        @NotNull(message = "Quantity is required.")
        @Min(value = 1, message = "Quantity should be at least 1.")
        @Positive(message = "Quantity should be greater than 0.")
        Long quantity

) implements Serializable {}