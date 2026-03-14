package com.hospital.hospital_management_backend.dto.request;

import com.hospital.hospital_management_backend.validation.PasswordValid;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * DTO for user creation requests.
 * Validates personal details, credentials, and role assignment.
 */
public record UserDtoRequest(

        @NotEmpty(message = "First name is required.")
        @NotBlank(message = "First name should not be blank.")
        @Size(min = 3, message = "First name should have at least {min} characters.")
        String firstName,

        @NotEmpty(message = "Last name is required.")
        @NotBlank(message = "Last name should not be blank.")
        @Size(min = 3, message = "Last name should have at least {min} characters.")
        String lastName,

        @NotEmpty(message = "CIN is required.")
        @NotBlank(message = "CIN should not be blank.")
        @Size(min = 5, message = "CIN should have at least {min} characters.")
        String cin,

        @NotEmpty(message = "Email is required.")
        @NotBlank(message = "Email should not be blank.")
        @Email(message = "Email should be valid.")
        String email,

        @NotEmpty(message = "Password is required.")
        @NotBlank(message = "Password should not be blank.")
        @PasswordValid
        String password,

        @NotNull(message = "Role is required.")
        RoleDtoRequest role

) implements Serializable {}