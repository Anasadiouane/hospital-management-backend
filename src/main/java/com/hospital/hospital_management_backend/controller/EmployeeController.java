package com.hospital.hospital_management_backend.controller;

import com.hospital.hospital_management_backend.dto.request.EmployeeDtoRequest;
import com.hospital.hospital_management_backend.dto.request.ServiceDtoRequest;
import com.hospital.hospital_management_backend.dto.response.EmployeeDtoResponse;
import com.hospital.hospital_management_backend.dto.response.FileDtoResponse;
import com.hospital.hospital_management_backend.dto.response.PageDtoResponse;
import com.hospital.hospital_management_backend.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // ---------- CREATE ----------
    @PostMapping
    public ResponseEntity<EmployeeDtoResponse> createEmployee(@Valid @RequestBody EmployeeDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(dto));
    }

    // ---------- READ ----------
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDtoResponse> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping("/cin/{cin}")
    public ResponseEntity<EmployeeDtoResponse> getEmployeeByCin(@PathVariable String cin) {
        return ResponseEntity.ok(employeeService.getEmployeeByCin(cin));
    }

    @GetMapping("/registration-number/{rn}")
    public ResponseEntity<EmployeeDtoResponse> getEmployeeByRN(@PathVariable String rn) {
        return ResponseEntity.ok(employeeService.getEmployeeByRN(rn));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDtoResponse>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/paginated")
    public ResponseEntity<PageDtoResponse> getEmployeesPaginated(
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        return ResponseEntity.ok(
                employeeService.getEmployeesPaginated(pageNumber, pageSize, sortField, sortDirection));
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<PageDtoResponse> getEmployeesByService(
            @PathVariable Long serviceId,
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        return ResponseEntity.ok(
                employeeService.getEmployeesByService(serviceId, pageNumber, pageSize, sortField, sortDirection));
    }

    // ---------- UPDATE ----------
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDtoResponse> updateEmployee(@PathVariable Long id,
                                                              @Valid @RequestBody EmployeeDtoRequest dto) {
        return ResponseEntity.ok(employeeService.updateEmployee(dto, id));
    }

    @PutMapping("/{employeeId}/service")
    public ResponseEntity<EmployeeDtoResponse> addServiceToEmployee(@PathVariable Long employeeId,
                                                                    @Valid @RequestBody ServiceDtoRequest dto) {
        return ResponseEntity.ok(employeeService.addServiceToEmployee(employeeId, dto));
    }

    // ---------- DOCUMENTS ----------
    @GetMapping("/{employeeId}/documents")
    public ResponseEntity<List<FileDtoResponse>> getDocumentsOfEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeService.getDocumentsOfEmployee(employeeId));
    }

    @PostMapping("/{employeeId}/documents/{documentId}")
    public ResponseEntity<EmployeeDtoResponse> addDocumentToEmployee(@PathVariable Long employeeId,
                                                                     @PathVariable Long documentId) {
        return ResponseEntity.ok(employeeService.addDocumentToEmployee(employeeId, documentId));
    }

    @DeleteMapping("/{employeeId}/documents/{documentId}")
    public ResponseEntity<Void> deleteDocumentOfEmployee(@PathVariable Long employeeId,
                                                         @PathVariable Long documentId) {
        employeeService.deleteDocumentOfEmployee(employeeId, documentId);
        return ResponseEntity.noContent().build();
    }

    // ---------- DELETE ----------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
