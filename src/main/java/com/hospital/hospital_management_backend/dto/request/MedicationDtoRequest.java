package com.hospital.hospital_management_backend.dto.request;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for medication creation requests.
 * Validates name, description, manufacturer, and price.
 */
public record MedicationDtoRequest(

        @NotBlank(message = "Name should not be blank.")
        String name,

        @NotBlank(message = "Description should not be blank.")
        @Size(min = 10, max = 255, message = "Description must be between {min} and {max} characters.")
        String description,

        @NotBlank(message = "Manufacturer should not be blank.")
        String manufacturer,

        @NotNull(message = "Price is required.")
        @Positive(message = "Price must be a positive number.")
        BigDecimal price

) implements Serializable {}