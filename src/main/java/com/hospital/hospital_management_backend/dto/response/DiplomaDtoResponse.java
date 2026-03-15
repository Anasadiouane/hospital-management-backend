package com.hospital.hospital_management_backend.dto.response;

import java.io.Serializable;
import java.time.LocalDate;


public record DiplomaDtoResponse(Long id, String title, LocalDate startDate, LocalDate endDate,
                                 Long documentId, Long employeeId) implements Serializable {
}