package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.RoleDtoRequest;
import com.hospital.hospital_management_backend.dto.response.RoleDtoResponse;
import com.hospital.hospital_management_backend.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for converting between Role entity and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    // REQUEST → ENTITY
    @Mapping(target = "id", ignore = true)
    Role toEntity(RoleDtoRequest dto);

    // ENTITY → RESPONSE
    RoleDtoResponse toDto(Role role);

    // LIST
    List<RoleDtoResponse> toDtoList(List<Role> roles);
}