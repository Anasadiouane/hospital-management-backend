package com.hospital.hospital_management_backend.service.impl;

import com.hospital.hospital_management_backend.dto.request.DiplomaDtoRequest;
import com.hospital.hospital_management_backend.dto.response.DiplomaDtoResponse;
import com.hospital.hospital_management_backend.entity.Diploma;
import com.hospital.hospital_management_backend.entity.File;
import com.hospital.hospital_management_backend.entity.Employee;
import com.hospital.hospital_management_backend.exception.AppException;
import com.hospital.hospital_management_backend.exception.ResourceNotFoundException;
import com.hospital.hospital_management_backend.mapper.DiplomaMapper;
import com.hospital.hospital_management_backend.repository.DiplomaRepository;
import com.hospital.hospital_management_backend.repository.EmployeeRepository;
import com.hospital.hospital_management_backend.repository.FileRepository;
import com.hospital.hospital_management_backend.service.DiplomaService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing Diplomas.
 */
@Service
public class DiplomaServiceImpl implements DiplomaService {

    private final DiplomaRepository diplomaRepository;
    private final FileRepository fileRepository;
    private final EmployeeRepository employeeRepository;
    private final DiplomaMapper diplomaMapper;

    public DiplomaServiceImpl(DiplomaRepository diplomaRepository,
                              FileRepository fileRepository,
                              EmployeeRepository employeeRepository,
                              DiplomaMapper diplomaMapper) {
        this.diplomaRepository = diplomaRepository;
        this.fileRepository = fileRepository;
        this.employeeRepository = employeeRepository;
        this.diplomaMapper = diplomaMapper;
    }

    @Override
    @Transactional
    public DiplomaDtoResponse createDiploma(Long employeeId, DiplomaDtoRequest diplomaDto) {
        Diploma diploma = diplomaMapper.diplomaDtoRequestToDiploma(diplomaDto);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId.toString()));

        diploma.setEmployee(employee);

        Diploma savedDiploma = diplomaRepository.save(diploma);

        return diplomaMapper.diplomaToDiplomaDtoResponse(savedDiploma);
    }

    @Override
    public DiplomaDtoResponse getDiplomaById(Long employeeId, Long diplomaId) {
        Diploma diploma = checkDiplomaByEmployee(employeeId, diplomaId);
        return diplomaMapper.diplomaToDiplomaDtoResponse(diploma);
    }

    @Override
    public List<DiplomaDtoResponse> getAllDiploma() {
        return diplomaRepository.findAll().stream()
                .map(diplomaMapper::diplomaToDiplomaDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DiplomaDtoResponse> getEmployeeDiplomas(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId.toString()));

        return employee.getDiplomas().stream()
                .map(diplomaMapper::diplomaToDiplomaDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DiplomaDtoResponse updateDiploma(Long employeeId, Long diplomaId, DiplomaDtoRequest diplomaDto) {
        Diploma diploma = checkDiplomaByEmployee(employeeId, diplomaId);

        if (StringUtils.hasText(diplomaDto.title())) {
            diploma.setTitle(diplomaDto.title());
        }
        if (diplomaDto.startDate() != null) {
            diploma.setStartDate(diplomaDto.startDate());
        }
        if (diplomaDto.endDate() != null) {
            diploma.setEndDate(diplomaDto.endDate());
        }

        Diploma updatedDiploma = diplomaRepository.save(diploma);

        return diplomaMapper.diplomaToDiplomaDtoResponse(updatedDiploma);
    }

    @Override
    @Transactional
    public DiplomaDtoResponse addDocumentToDiploma(Long employeeId, Long diplomaId, Long documentId) {
        checkDiplomaByEmployee(employeeId, diplomaId);

        Diploma diploma = diplomaRepository.findById(diplomaId)
                .orElseThrow(() -> new ResourceNotFoundException("Diploma", "id", diplomaId.toString()));

        File document = fileRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("File", "id", documentId.toString()));

        diploma.setDocument(document);

        Diploma updatedDiploma = diplomaRepository.save(diploma);

        return diplomaMapper.diplomaToDiplomaDtoResponse(updatedDiploma);
    }

    @Override
    public void deleteDiploma(Long employeeId, Long diplomaId) {
        Diploma diploma = checkDiplomaByEmployee(employeeId, diplomaId);
        diplomaRepository.delete(diploma);
    }

    private Diploma checkDiplomaByEmployee(Long employeeId, Long diplomaId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId.toString()));

        Diploma diploma = diplomaRepository.findById(diplomaId)
                .orElseThrow(() -> new ResourceNotFoundException("Diploma", "id", diplomaId.toString()));

        if (!diploma.getEmployee().getId().equals(employee.getId())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Diploma does not belong to employee");
        }
        return diploma;
    }
}