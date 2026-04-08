package com.hospital.hospital_management_backend.service.impl;

import com.hospital.hospital_management_backend.dto.request.EmployeeDtoRequest;
import com.hospital.hospital_management_backend.dto.request.ServiceDtoRequest;
import com.hospital.hospital_management_backend.dto.response.EmployeeDtoResponse;
import com.hospital.hospital_management_backend.dto.response.FileDtoResponse;
import com.hospital.hospital_management_backend.dto.response.PageDtoResponse;
import com.hospital.hospital_management_backend.entity.Employee;
import com.hospital.hospital_management_backend.entity.File;
import com.hospital.hospital_management_backend.entity.HospitalService;
import com.hospital.hospital_management_backend.exception.AppException;
import com.hospital.hospital_management_backend.exception.ResourceNotFoundException;
import com.hospital.hospital_management_backend.mapper.EmployeeMapper;
import com.hospital.hospital_management_backend.mapper.FileMapper;
import com.hospital.hospital_management_backend.repository.EmployeeRepository;
import com.hospital.hospital_management_backend.repository.FileRepository;
import com.hospital.hospital_management_backend.repository.ServiceRepository;
import com.hospital.hospital_management_backend.service.EmployeeService;
import com.hospital.hospital_management_backend.util.SortUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ServiceRepository serviceRepository;
    private final EmployeeMapper employeeMapper;
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               ServiceRepository serviceRepository,
                               EmployeeMapper employeeMapper,
                               FileRepository fileRepository,
                               FileMapper fileMapper) {
        this.employeeRepository = employeeRepository;
        this.serviceRepository = serviceRepository;
        this.employeeMapper = employeeMapper;
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
    }

    // =========================
    // CREATE
    // =========================
    @Override
    @Transactional
    public EmployeeDtoResponse createEmployee(EmployeeDtoRequest dto) {
        Employee employee = employeeMapper.toEntity(dto);

        HospitalService service = getServiceByName(dto.service().name());
        employee.setHospitalService(service);

        return employeeMapper.toDto(employeeRepository.save(employee));
    }

    // =========================
    // READ
    // =========================
    @Override
    @Transactional(readOnly = true)
    public EmployeeDtoResponse getEmployeeById(Long id) {
        return employeeMapper.toDto(findEmployee(id));
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDtoResponse getEmployeeByCin(String cin) {
        return employeeMapper.toDto(
                employeeRepository.findByCin(cin)
                        .orElseThrow(() -> new ResourceNotFoundException("Employee", "cin", cin))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDtoResponse getEmployeeByRN(String rn) {
        return employeeMapper.toDto(
                employeeRepository.findByRegistrationNumber(rn)
                        .orElseThrow(() -> new ResourceNotFoundException("Employee", "registrationNumber", rn))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDtoResponse> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public PageDtoResponse getEmployeesPaginated(Integer pageNumber,
                                                 Integer pageSize,
                                                 String sortField,
                                                 String sortDirection) {
        PageRequest pageRequest =
                SortUtil.buildPageRequest(pageNumber, pageSize, sortField, sortDirection);

        Page<Employee> employeePage = employeeRepository.findAll(pageRequest);

        List<EmployeeDtoResponse> list = employeePage.getContent()
                .stream()
                .map(employeeMapper::toDto)
                .toList();

        return SortUtil.generatePageDtoResponse(
                list,
                employeePage.getNumber(),
                employeePage.getSize(),
                employeePage.getTotalElements(),
                employeePage.getTotalPages(),
                employeePage.isLast()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PageDtoResponse getEmployeesByService(Long serviceId,
                                                 Integer pageNumber,
                                                 Integer pageSize,
                                                 String sortField,
                                                 String sortDirection) {
        HospitalService hospitalService = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", serviceId.toString()));

        PageRequest pageRequest =
                SortUtil.buildPageRequest(pageNumber, pageSize, sortField, sortDirection);

        Page<Employee> employeePage =
                employeeRepository.findAllByHospitalServiceId(hospitalService.getId(), pageRequest);

        List<EmployeeDtoResponse> list = employeePage.getContent()
                .stream()
                .map(employeeMapper::toDto)
                .toList();

        return SortUtil.generatePageDtoResponse(
                list,
                employeePage.getNumber(),
                employeePage.getSize(),
                employeePage.getTotalElements(),
                employeePage.getTotalPages(),
                employeePage.isLast()
        );
    }

    // =========================
    // UPDATE
    // =========================
    @Override
    @Transactional
    public EmployeeDtoResponse updateEmployee(EmployeeDtoRequest dto, Long id) {
        Employee employee = findEmployee(id);

        employeeMapper.updateEmployeeFromDto(dto, employee);

        if (dto.service() != null && dto.service().name() != null) {
            employee.setHospitalService(getServiceByName(dto.service().name()));
        }

        return employeeMapper.toDto(employeeRepository.save(employee));
    }

    // =========================
    // DOCUMENTS
    // =========================
    @Override
    @Transactional(readOnly = true)
    public List<FileDtoResponse> getDocumentsOfEmployee(Long employeeId) {
        Employee employee = findEmployee(employeeId);
        return fileMapper.toDtoList(new ArrayList<>(employee.getDocuments()));
    }

    @Override
    @Transactional
    public EmployeeDtoResponse addDocumentToEmployee(Long employeeId, Long documentId) {
        Employee employee = findEmployee(employeeId);
        File document = findFile(documentId);

        if (employee.getDocuments() == null) {
            employee.setDocuments(new HashSet<>());
        }

        employee.getDocuments().add(document);

        return employeeMapper.toDto(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public void deleteDocumentOfEmployee(Long employeeId, Long documentId) {
        Employee employee = findEmployee(employeeId);
        File document = findFile(documentId);

        checkIfDocumentBelongs(employee, documentId);

        employee.getDocuments().removeIf(f -> f.getId().equals(documentId));

        employeeRepository.save(employee);
        fileRepository.delete(document);
    }

    // =========================
    // SERVICE ASSIGNMENT
    // =========================
    @Override
    @Transactional
    public EmployeeDtoResponse addServiceToEmployee(Long employeeId, ServiceDtoRequest dto) {
        Employee employee = findEmployee(employeeId);
        employee.setHospitalService(getServiceByName(dto.name()));
        return employeeMapper.toDto(employeeRepository.save(employee));
    }

    // =========================
    // DELETE
    // =========================
    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        employeeRepository.delete(findEmployee(id));
    }

    // =========================
    // HELPERS
    // =========================
    private Employee findEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id.toString()));
    }

    private File findFile(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File", "id", id.toString()));
    }

    private HospitalService getServiceByName(String name) {
        return serviceRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "name", name));
    }

    private void checkIfDocumentBelongs(Employee employee, Long docId) {
        boolean exists = employee.getDocuments()
                .stream()
                .anyMatch(d -> d.getId().equals(docId));

        if (!exists) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Document does not belong to employee");
        }
    }
}