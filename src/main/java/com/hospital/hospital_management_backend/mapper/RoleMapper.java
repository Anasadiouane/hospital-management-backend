package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.RoleDtoRequest;
import com.hospital.hospital_management_backend.dto.response.RoleDtoResponse;
import com.hospital.hospital_management_backend.entity.Role;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting between Role entity and DTOs.
 */
@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role roleDtoRequestToRole(RoleDtoRequest roleDtoRequest);

    RoleDtoResponse roleToRoleDtoResponse(Role role);
}