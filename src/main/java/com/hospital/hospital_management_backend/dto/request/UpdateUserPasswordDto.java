package com.hospital.hospital_management_backend.dto.request;

import com.hospital.hospital_management_backend.validation.PasswordValid;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * DTO for updating a user's password.
 * Validates old password, new password, and confirmation.
 */
public record UpdateUserPasswordDto(

        @NotEmpty(message = "Old password is required.")
        String oldPassword,

        @NotEmpty(message = "New password is required.")
        @PasswordValid
        String newPassword,

        @NotEmpty(message = "Confirm password is required.")
        @PasswordValid
        String confirmNewPassword

) implements Serializable {}