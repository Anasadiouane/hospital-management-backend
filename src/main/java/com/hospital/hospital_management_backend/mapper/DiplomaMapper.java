package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.DiplomaDtoRequest;
import com.hospital.hospital_management_backend.dto.response.DiplomaDtoResponse;
import com.hospital.hospital_management_backend.entity.Diploma;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting between Diploma entity and DTOs.
 */
@Mapper(componentModel = "spring")
public interface DiplomaMapper {

    @Mapping(target = "documentId", source = "document.id")
    @Mapping(target = "employeeId", source = "employee.id")
    DiplomaDtoResponse diplomaToDiplomaDtoResponse(Diploma diploma);

    @Mapping(target = "document.id", source = "documentId")
    @Mapping(target = "employee.id", source = "employeeId")
    Diploma diplomaDtoRequestToDiploma(DiplomaDtoRequest diplomaDtoRequest);
}