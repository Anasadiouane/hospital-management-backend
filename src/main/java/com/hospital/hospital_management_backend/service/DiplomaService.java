package com.hospital.hospital_management_backend.service;

import com.hospital.hospital_management_backend.dto.request.DiplomaDtoRequest;
import com.hospital.hospital_management_backend.dto.response.DiplomaDtoResponse;

import java.util.List;

public interface DiplomaService {
    DiplomaDtoResponse createDiploma(Long employeeId, DiplomaDtoRequest diplomaDto);
    DiplomaDtoResponse getDiplomaById(Long employeeId, Long diplomaId);
    List<DiplomaDtoResponse> getAllDiploma();
    List<DiplomaDtoResponse> getEmployeeDiplomas(Long employeeId);
    DiplomaDtoResponse updateDiploma(Long employeeId, Long diplomaId, DiplomaDtoRequest diplomaDto);
    DiplomaDtoResponse addDocumentToDiploma(Long employeeId, Long diplomaId, Long documentId);
    void deleteDiploma(Long employeeId, Long diplomaId);
}