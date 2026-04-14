package com.hospital.hospital_management_backend.service;

import com.hospital.hospital_management_backend.dto.request.VacationDtoRequest;
import com.hospital.hospital_management_backend.dto.response.VacationDtoResponse;

import java.util.List;

public interface VacationService {
    VacationDtoResponse createVacation(Long employeeId, VacationDtoRequest vacationDto);
    VacationDtoResponse getVacationById(Long employeeId, Long vacationId);
    List<VacationDtoResponse> getEmployeeVacations(Long employeeId);
    List<VacationDtoResponse> getAllVacations();
    VacationDtoResponse updateVacation(Long employeeId, Long vacationId, VacationDtoRequest vacationDto);
    void deleteVacation(Long employeeId, Long vacationId);
}