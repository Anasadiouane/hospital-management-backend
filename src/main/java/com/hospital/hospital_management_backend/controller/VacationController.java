package com.hospital.hospital_management_backend.controller;

import com.hospital.hospital_management_backend.dto.request.VacationDtoRequest;
import com.hospital.hospital_management_backend.dto.response.VacationDtoResponse;
import com.hospital.hospital_management_backend.service.VacationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class VacationController {

    private final VacationService vacationService;

    public VacationController(VacationService vacationService) {
        this.vacationService = vacationService;
    }

    @PostMapping("/employees/{employeeId}/vacations")
    public ResponseEntity<VacationDtoResponse> createVacation(@PathVariable Long employeeId,
                                                              @Valid @RequestBody VacationDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vacationService.createVacation(employeeId, dto));
    }

    @GetMapping("/employees/{employeeId}/vacations/{vacationId}")
    public ResponseEntity<VacationDtoResponse> getVacationById(@PathVariable Long employeeId,
                                                               @PathVariable Long vacationId) {
        return ResponseEntity.ok(vacationService.getVacationById(employeeId, vacationId));
    }

    @GetMapping("/employees/{employeeId}/vacations")
    public ResponseEntity<List<VacationDtoResponse>> getEmployeeVacations(@PathVariable Long employeeId) {
        return ResponseEntity.ok(vacationService.getEmployeeVacations(employeeId));
    }

    @GetMapping("/vacations")
    public ResponseEntity<List<VacationDtoResponse>> getAllVacations() {
        return ResponseEntity.ok(vacationService.getAllVacations());
    }

    @PutMapping("/employees/{employeeId}/vacations/{vacationId}")
    public ResponseEntity<VacationDtoResponse> updateVacation(@PathVariable Long employeeId,
                                                              @PathVariable Long vacationId,
                                                              @Valid @RequestBody VacationDtoRequest dto) {
        return ResponseEntity.ok(vacationService.updateVacation(employeeId, vacationId, dto));
    }

    @DeleteMapping("/employees/{employeeId}/vacations/{vacationId}")
    public ResponseEntity<Void> deleteVacation(@PathVariable Long employeeId,
                                               @PathVariable Long vacationId) {
        vacationService.deleteVacation(employeeId, vacationId);
        return ResponseEntity.noContent().build();
    }
}
