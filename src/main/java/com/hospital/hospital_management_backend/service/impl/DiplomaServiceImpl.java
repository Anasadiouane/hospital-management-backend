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

import java.util.List;

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
    public DiplomaDtoResponse createDiploma(Long employeeId, DiplomaDtoRequest dto) {
        dto.isStartBeforeEnd();
        Employee employee = getEmployee(employeeId);

        Diploma diploma = diplomaMapper.toEntity(dto);
        diploma.setEmployee(employee);

        return diplomaMapper.toDto(diplomaRepository.save(diploma));
    }

    @Override
    @Transactional(readOnly = true)
    public DiplomaDtoResponse getDiplomaById(Long employeeId, Long diplomaId) {
        Diploma diploma = checkDiplomaByEmployee(employeeId, diplomaId);
        return diplomaMapper.toDto(diploma);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiplomaDtoResponse> getAllDiploma() {
        return diplomaMapper.toDtoList(diplomaRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiplomaDtoResponse> getEmployeeDiplomas(Long employeeId) {
        getEmployee(employeeId); // validate employee exists
        return diplomaMapper.toDtoList(diplomaRepository.findByEmployeeId(employeeId));
    }

    @Override
    @Transactional
    public DiplomaDtoResponse updateDiploma(Long employeeId, Long diplomaId, DiplomaDtoRequest dto) {
        dto.isStartBeforeEnd();
        Diploma diploma = checkDiplomaByEmployee(employeeId, diplomaId);

        diplomaMapper.updateDiplomaFromDto(dto, diploma);

        return diplomaMapper.toDto(diplomaRepository.save(diploma));
    }

    @Override
    @Transactional
    public DiplomaDtoResponse addDocumentToDiploma(Long employeeId, Long diplomaId, Long documentId) {
        Diploma diploma = checkDiplomaByEmployee(employeeId, diplomaId);

        File document = fileRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("File", "id", documentId.toString()));

        diploma.setDocument(document);

        return diplomaMapper.toDto(diplomaRepository.save(diploma));
    }

    @Override
    @Transactional
    public void deleteDiploma(Long employeeId, Long diplomaId) {
        Diploma diploma = checkDiplomaByEmployee(employeeId, diplomaId);
        diplomaRepository.delete(diploma);
    }

    // ─── Helpers ────────────────────────────────────────────────────────────────

    private Employee getEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId.toString()));
    }

    private Diploma checkDiplomaByEmployee(Long employeeId, Long diplomaId) {
        getEmployee(employeeId); // validate employee exists first

        Diploma diploma = diplomaRepository.findById(diplomaId)
                .orElseThrow(() -> new ResourceNotFoundException("Diploma", "id", diplomaId.toString()));

        if (!diploma.getEmployee().getId().equals(employeeId)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Diploma does not belong to employee");
        }

        return diploma;
    }
}