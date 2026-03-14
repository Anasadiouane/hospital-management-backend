package com.hospital.hospital_management_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for vacation request submissions.
 * Validates start and end dates, and vacation type.
 */
public record VacationDtoRequest(

        @NotNull(message = "Start date is required.")
        @Future(message = "Start date must be in the future.")
        LocalDateTime startDate,

        @NotNull(message = "End date is required.")
        @Future(message = "End date must be in the future.")
        LocalDateTime endDate,

        boolean isSickVacation

) implements Serializable {

    @JsonIgnore
    public boolean isEndDateAfterStartDate() {
        return endDate.isAfter(startDate);
    }
}