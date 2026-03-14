package com.hospital.hospital_management_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * DTO for user login requests.
 * Validates CIN and password fields.
 */
public record LoginRequestDto(

        @NotNull(message = "CIN is required.")
        @NotBlank(message = "CIN should not be blank.")
        String cin,

        @NotNull(message = "Password is required.")
        @NotBlank(message = "Password should not be blank.")
        String password

) implements Serializable {}