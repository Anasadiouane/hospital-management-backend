package com.hospital.hospital_management_backend.dto.request;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for employee creation requests.
 * Validates personal and employment details.
 */
public record EmployeeDtoRequest(

        @NotBlank(message = "First name should not be blank")
        @Size(min = 3, message = "First name must contain at least 3 characters")
        String firstName,

        @NotBlank(message = "Last name should not be blank")
        @Size(min = 3, message = "Last name must contain at least 3 characters")
        String lastName,

        @NotBlank(message = "CIN should not be blank")
        @Size(min = 5, message = "CIN must contain at least 5 characters")
        String cin,

        @NotBlank(message = "Registration number should not be blank")
        @Size(min = 5, message = "Registration number must contain at least 5 characters")
        String registrationNumber,

        @NotNull(message = "Recruitment date is required")
        @Past(message = "Recruitment date should be in the past")
        LocalDate recruitmentDate,

        @NotNull(message = "Service is required")
        ServiceDtoRequest service

) implements Serializable {}