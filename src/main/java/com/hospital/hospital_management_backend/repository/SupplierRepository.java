package com.hospital.hospital_management_backend.repository;

import com.hospital.hospital_management_backend.entity.Supplier;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    boolean existsByPhone(@NotNull(message = "Phone number is required.") @NotBlank(message = "Phone number should not be blank.") @Pattern(
                regexp = "^(\\+212|0)([5-7])\\d{8}$",
                message = "Phone number should be a valid Moroccan phone number."
        ) String phone);

    boolean existsByEmail(@NotNull(message = "Email is required.") @NotBlank(message = "Email should not be blank.") @Email(message = "Invalid email format.") String email);
}