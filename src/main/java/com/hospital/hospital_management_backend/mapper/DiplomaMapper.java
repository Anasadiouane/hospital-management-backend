package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.DiplomaDtoRequest;
import com.hospital.hospital_management_backend.dto.response.DiplomaDtoResponse;
import com.hospital.hospital_management_backend.entity.Diploma;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for converting between Diploma entity and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiplomaMapper {

    // Entity → Response DTO
    @Mapping(source = "document.id", target = "documentId")
    @Mapping(source = "employee.id", target = "employeeId")
    DiplomaDtoResponse toDto(Diploma diploma);

    // list mapping
    List<DiplomaDtoResponse> toDtoList(List<Diploma> diplomas);

    // Request DTO → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "document", ignore = true)
    @Mapping(target = "employee", ignore = true)
    Diploma toEntity(DiplomaDtoRequest dto);

    // UPDATE (PATCH style)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "document", ignore = true)
    @Mapping(target = "employee", ignore = true)
    void updateDiplomaFromDto(DiplomaDtoRequest dto, @MappingTarget Diploma diploma);
}