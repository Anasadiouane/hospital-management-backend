package com.hospital.hospital_management_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;

/**
 * DTO for associating an image with a user.
 * Validates image identity.
 */
public record ImageToUserDtoRequest(

        @NotNull(message = "Image ID is required.")
        @Positive(message = "Image ID must be a positive number.")
        Long imageId

) implements Serializable {}