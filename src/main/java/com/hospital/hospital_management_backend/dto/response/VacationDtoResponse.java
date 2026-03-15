package com.hospital.hospital_management_backend.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;


public record VacationDtoResponse(Long id, LocalDateTime startDate, LocalDateTime endDate,
                                  boolean isSickVacation, Long employeeId) implements Serializable {
}