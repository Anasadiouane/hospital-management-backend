package com.hospital.hospital_management_backend.dto.request;

import com.hospital.hospital_management_backend.validation.PasswordValid;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/** Used for the admin password reset (no old password needed). */
public record ChangePasswordDto(

        @NotBlank(message = "New password should not be blank.")
        @PasswordValid
        String newPassword,

        @NotBlank(message = "Confirm password should not be blank.")
        @PasswordValid
        String confirmNewPassword

) implements Serializable {}
