package com.hospital.hospital_management_backend.dto.response;

import java.io.Serializable;
import java.time.LocalDate;


public record EmployeeDtoResponse(Long id, String firstName, String lastName, String cin, String registrationNumber,
                                  LocalDate recruitmentDate, String serviceName) implements Serializable {
}