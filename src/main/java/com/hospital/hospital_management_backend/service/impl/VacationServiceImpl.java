package com.hospital.hospital_management_backend.service.impl;

import com.hospital.hospital_management_backend.dto.request.VacationDtoRequest;
import com.hospital.hospital_management_backend.dto.response.VacationDtoResponse;
import com.hospital.hospital_management_backend.entity.Employee;
import com.hospital.hospital_management_backend.entity.Vacation;
import com.hospital.hospital_management_backend.exception.AppException;
import com.hospital.hospital_management_backend.exception.ResourceNotFoundException;
import com.hospital.hospital_management_backend.mapper.VacationMapper;
import com.hospital.hospital_management_backend.repository.EmployeeRepository;
import com.hospital.hospital_management_backend.repository.VacationRepository;
import com.hospital.hospital_management_backend.service.VacationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * Service implementation for managing vacations.
 */
@Service
public class VacationServiceImpl implements VacationService {

    private final VacationRepository vacationRepository;
    private final EmployeeRepository employeeRepository;
    private final VacationMapper vacationMapper;

    public VacationServiceImpl(VacationRepository vacationRepository,
                               EmployeeRepository employeeRepository,
                               VacationMapper vacationMapper) {
        this.vacationRepository = vacationRepository;
        this.employeeRepository = employeeRepository;
        this.vacationMapper = vacationMapper;
    }

    // =========================
    // CREATE
    // =========================
    @Override
    @Transactional
    public VacationDtoResponse createVacation(Long employeeId, VacationDtoRequest dto) {
        if (!dto.isEndDateAfterStartDate()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "End date must be after start date");
        }

        Employee employee = findEmployee(employeeId);

        Vacation vacation = vacationMapper.toEntity(dto);
        vacation.setEmployee(employee);

        return vacationMapper.toDto(vacationRepository.save(vacation));
    }

    // =========================
    // READ
    // =========================
    @Override
    @Transactional(readOnly = true)
    public VacationDtoResponse getVacationById(Long employeeId, Long vacationId) {
        return vacationMapper.toDto(checkVacationByEmployee(employeeId, vacationId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacationDtoResponse> getAllVacations() {
        return vacationMapper.toDtoList(vacationRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacationDtoResponse> getEmployeeVacations(Long employeeId) {
        Employee employee = findEmployee(employeeId);
        return vacationMapper.toDtoList(new ArrayList<>(employee.getVacations()));
    }

    // =========================
    // UPDATE
    // =========================
    @Override
    @Transactional
    public VacationDtoResponse updateVacation(Long employeeId, Long vacationId, VacationDtoRequest dto) {
        if (!dto.isEndDateAfterStartDate()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "End date must be after start date");
        }

        Vacation vacation = checkVacationByEmployee(employeeId, vacationId);

        // ✅ use MapStruct PATCH instead of manual Optional setters
        vacationMapper.updateFromDto(dto, vacation);

        return vacationMapper.toDto(vacationRepository.save(vacation));
    }

    // =========================
    // DELETE
    // =========================
    @Override
    @Transactional
    public void deleteVacation(Long employeeId, Long vacationId) {
        vacationRepository.delete(checkVacationByEmployee(employeeId, vacationId));
    }

    // =========================
    // HELPERS
    // =========================
    private Employee findEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id.toString()));
    }

    private Vacation checkVacationByEmployee(Long employeeId, Long vacationId) {
        findEmployee(employeeId); // validate employee exists

        Vacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new ResourceNotFoundException("Vacation", "id", vacationId.toString()));

        if (!vacation.getEmployee().getId().equals(employeeId)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Vacation does not belong to employee");
        }

        return vacation;
    }
}