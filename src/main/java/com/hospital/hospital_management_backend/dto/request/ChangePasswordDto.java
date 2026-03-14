package com.hospital.hospital_management_backend.dto.request;

import com.hospital.hospital_management_backend.validation.PasswordValid;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * DTO for changing a user's password.
 * Validates old password, new password, and confirmation.
 */
public record ChangePasswordDto(

        @NotBlank(message = "Old password should not be blank.")
        String oldPassword,

        @NotBlank(message = "New password should not be blank.")
        @PasswordValid
        String newPassword,

        @NotBlank(message = "Confirm password should not be blank.")
        @PasswordValid
        String confirmNewPassword

) implements Serializable {}