package com.hospital.hospital_management_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for hospital service creation requests.
 * Validates service name format and presence.
 */
public record ServiceDtoRequest(

        @NotBlank(message = "Service name is required.")
        @Size(min = 3, max = 50, message = "Service name must be between {min} and {max} characters.")
        String name

) implements Serializable {}