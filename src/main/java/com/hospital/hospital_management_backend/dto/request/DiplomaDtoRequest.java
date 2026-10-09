package com.hospital.hospital_management_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.hospital_management_backend.exception.AppException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDate;

public record DiplomaDtoRequest(

        @NotBlank(message = "Title should not be blank")
        String title,

        @NotNull(message = "Start date is required")
        @Past(message = "Start date should be in the past")
        LocalDate startDate,

        @NotNull(message = "End date is required")
        @Past(message = "End date should be in the past")
        LocalDate endDate

) implements Serializable {

    /** Throws AppException if startDate is not before endDate. */
    @JsonIgnore
    public void isStartBeforeEnd() {
        if (startDate == null || endDate == null || !startDate.isBefore(endDate)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Start date must be before end date");
        }
    }
}
