package com.hospital.hospital_management_backend.dto.request;

import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * DTO for material creation requests.
 * Validates name and description fields.
 */
public record MaterialDtoRequest(

        @NotNull(message = "Name is required.")
        @NotBlank(message = "Name should not be blank.")
        @Size(min = 3, max = 50, message = "Name must be between {min} and {max} characters.")
        String name,

        @NotNull(message = "Description is required.")
        @NotBlank(message = "Description should not be blank.")
        @Size(min = 10, max = 255, message = "Description must be between {min} and {max} characters.")
        String description

) implements Serializable {}