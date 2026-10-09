package com.hospital.hospital_management_backend.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.io.Serializable;

public record UserUpdateDtoRequest(

        @NotBlank(message = "First name should not be blank.")
        @Size(min = 3, message = "First name should have at least {min} characters.")
        String firstName,

        @NotBlank(message = "Last name should not be blank.")
        @Size(min = 3, message = "Last name should have at least {min} characters.")
        String lastName,

        @NotBlank(message = "CIN should not be blank.")
        @Size(min = 5, message = "CIN should have at least {min} characters.")
        String cin,

        @NotBlank(message = "Email should not be blank.")
        @Email(message = "Enter a valid email.")
        String email,

        @NotNull(message = "Role is required.")
        @Valid
        RoleDtoRequest role

) implements Serializable {}
