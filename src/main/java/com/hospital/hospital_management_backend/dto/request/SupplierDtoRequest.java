package com.hospital.hospital_management_backend.dto.request;

import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * DTO for supplier creation requests.
 * Validates name, address, phone number, and email.
 */
public record SupplierDtoRequest(

        @NotNull(message = "Name is required.")
        @NotBlank(message = "Name should not be blank.")
        @Size(min = 3, max = 20, message = "Name must be between {min} and {max} characters.")
        String name,

        @NotNull(message = "Address is required.")
        @NotBlank(message = "Address should not be blank.")
        String address,

        @NotNull(message = "Phone number is required.")
        @NotBlank(message = "Phone number should not be blank.")
        @Pattern(
                regexp = "^(\\+212|0)([5-7])\\d{8}$",
                message = "Phone number should be a valid Moroccan phone number."
        )
        String phone,

        @NotNull(message = "Email is required.")
        @NotBlank(message = "Email should not be blank.")
        @Email(message = "Invalid email format.")
        String email

) implements Serializable {}