package com.hospital.hospital_management_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * DTO for role creation requests.
 * Validates role name format and presence.
 */
public record RoleDtoRequest(

        @NotBlank(message = "Role name is required.")
        @Pattern(regexp = "^ROLE_[A-Z_]+$", message = "Role name must start with 'ROLE_' and use uppercase letters.")
        String name

) implements Serializable {}