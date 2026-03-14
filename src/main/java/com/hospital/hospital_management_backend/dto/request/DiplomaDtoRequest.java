package com.hospital.hospital_management_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.hospital_management_backend.exception.AppException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for diploma creation requests.
 * Ensures title and dates are valid and logically consistent.
 */
public record DiplomaDtoRequest(

        @NotBlank(message = "Titre should not be blank")
        String titre,

        @Past(message = "Start date should be in the past")
        LocalDate startDate,

        @Past(message = "End date should be in the past")
        LocalDate endDate

) implements Serializable {

    /**
     * Validates that startDate is before endDate.
     * Throws AppException if the condition is violated.
     */
    @JsonIgnore
    public boolean isStartBeforeEnd() {
        boolean isValid = startDate.isBefore(endDate);
        if (!isValid) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Start date must be before end date");
        }
        return true;
    }
}