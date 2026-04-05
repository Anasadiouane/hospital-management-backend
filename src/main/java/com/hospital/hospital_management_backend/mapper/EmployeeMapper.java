package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.EmployeeDtoRequest;
import com.hospital.hospital_management_backend.dto.response.EmployeeDtoResponse;
import com.hospital.hospital_management_backend.entity.Employee;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for converting between Employee entity and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {

    // Entity → Response DTO
    @Mapping(source = "hospitalService.name", target = "serviceName")
    EmployeeDtoResponse toDto(Employee employee);

    List<EmployeeDtoResponse> toDtoList(List<Employee> employees);

    // Request DTO → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hospitalService", ignore = true)  // ✅ resolved in service
    @Mapping(target = "vacations", ignore = true)        // ✅ relations ignored
    @Mapping(target = "documents", ignore = true)        // ✅ relations ignored
    @Mapping(target = "diplomas", ignore = true)         // ✅ relations ignored
    Employee toEntity(EmployeeDtoRequest dto);

    // UPDATE (PATCH)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hospitalService", ignore = true)  // ✅ resolved in service
    @Mapping(target = "vacations", ignore = true)
    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "diplomas", ignore = true)
    void updateEmployeeFromDto(EmployeeDtoRequest dto, @MappingTarget Employee employee);
}