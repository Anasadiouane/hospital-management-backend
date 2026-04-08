package com.hospital.hospital_management_backend.service;

import com.hospital.hospital_management_backend.dto.request.EmployeeDtoRequest;
import com.hospital.hospital_management_backend.dto.request.ServiceDtoRequest;
import com.hospital.hospital_management_backend.dto.response.EmployeeDtoResponse;
import com.hospital.hospital_management_backend.dto.response.FileDtoResponse;
import com.hospital.hospital_management_backend.dto.response.PageDtoResponse;

import java.util.List;

public interface EmployeeService {
    EmployeeDtoResponse createEmployee(EmployeeDtoRequest employeeDto);
    EmployeeDtoResponse getEmployeeById(Long employeeId);
    EmployeeDtoResponse getEmployeeByCin(String cin);
    EmployeeDtoResponse getEmployeeByRN(String registrationNumber);
    List<EmployeeDtoResponse> getAllEmployees();
    List<FileDtoResponse> getDocumentsOfEmployee(Long employeeId);

    PageDtoResponse getEmployeesPaginated(Integer pageNumber,
                                          Integer pageSize,
                                          String sortField,
                                          String sortDirection);

    PageDtoResponse getEmployeesByService(Long serviceId, Integer pageNumber, Integer pageSize, String sortField, String sortDirection);
    EmployeeDtoResponse updateEmployee(EmployeeDtoRequest employeeDto, Long employeeId);
    EmployeeDtoResponse addDocumentToEmployee(Long employeeId, Long documentId);
    EmployeeDtoResponse addServiceToEmployee(Long employeeId, ServiceDtoRequest serviceDtoRequest);
    void deleteEmployee(Long employeeId);
    void deleteDocumentOfEmployee(Long employeeId, Long documentId);
}