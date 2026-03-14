package com.hospital.hospital_management_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;

/**
 * DTO for file-related requests.
 * Validates file identity and type.
 */
public record FileDtoRequest(

        @NotNull(message = "File ID is required.")
        @Positive(message = "File ID must be a positive number.")
        Long id,

        @NotBlank(message = "File type is required.")
        String type

) implements Serializable {}