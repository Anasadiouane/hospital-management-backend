package com.hospital.hospital_management_backend.service;

import com.hospital.hospital_management_backend.dto.request.RoleDtoRequest;
import com.hospital.hospital_management_backend.dto.response.RoleDtoResponse;

import java.util.List;

public interface RoleService {
    RoleDtoResponse createRole(RoleDtoRequest roleDto);
    RoleDtoResponse getRoleByName(String name);
    RoleDtoResponse getRoleById(Long roleId);
    List<RoleDtoResponse> getAllRoles();
    RoleDtoResponse updateRole(RoleDtoRequest roleDto, Long roleId);
    void deleteRole(Long roleId);
}