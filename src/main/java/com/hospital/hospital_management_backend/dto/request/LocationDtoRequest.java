package com.hospital.hospital_management_backend.dto.request;

import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * DTO for location creation requests.
 * Validates name, description, and coordinates.
 */
public record LocationDtoRequest(

        @NotBlank(message = "Name should not be blank.")
        @Size(min = 3, max = 50, message = "Name must be between {min} and {max} characters.")
        String name,

        @NotBlank(message = "Description should not be blank.")
        @Size(min = 5, max = 255, message = "Description must be between {min} and {max} characters.")
        String description,

        @NotBlank(message = "Address should not be blank.")
        @Size(min = 5, max = 100, message = "Address must be between {min} and {max} characters.")
        String address,

        @NotBlank(message = "Latitude should not be blank.")
        String latitude,

        @NotBlank(message = "Longitude should not be blank.")
        String longitude

) implements Serializable {}