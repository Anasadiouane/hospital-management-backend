package com.hospital.hospital_management_backend.service.impl;

import com.hospital.hospital_management_backend.dto.request.RoleDtoRequest;
import com.hospital.hospital_management_backend.dto.response.RoleDtoResponse;
import com.hospital.hospital_management_backend.entity.Role;
import com.hospital.hospital_management_backend.enums.ROLES;
import com.hospital.hospital_management_backend.exception.AppException;
import com.hospital.hospital_management_backend.exception.ResourceNotFoundException;
import com.hospital.hospital_management_backend.mapper.RoleMapper;
import com.hospital.hospital_management_backend.repository.RoleRepository;
import com.hospital.hospital_management_backend.auth.AuthenticationFacade;
import com.hospital.hospital_management_backend.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;


/**
 * Service implementation for managing roles.
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final AuthenticationFacade authenticationFacade;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository,
                           AuthenticationFacade authenticationFacade,
                           RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.authenticationFacade = authenticationFacade;
        this.roleMapper = roleMapper;
    }

    // =========================
    // CREATE
    // =========================
    @Override
    @Transactional
    public RoleDtoResponse createRole(RoleDtoRequest dto) {
        if (roleRepository.existsByName(dto.name())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Role already exists");
        }
        return roleMapper.toDto(roleRepository.save(roleMapper.toEntity(dto)));
    }

    // =========================
    // READ
    // =========================
    @Override
    @Transactional(readOnly = true)
    public RoleDtoResponse getRoleById(Long roleId) {
        return roleMapper.toDto(findRole(roleId));
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDtoResponse getRoleByName(String name) {
        return roleMapper.toDto(
                roleRepository.findByName(name)
                        .orElseThrow(() -> new ResourceNotFoundException("Role", "name", name))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDtoResponse> getAllRoles() {
        boolean isAdmin = authenticationFacade.getAuthentication().getAuthorities()
                .stream()
                .anyMatch(role -> role.getAuthority().equals(ROLES.ROLE_ADMIN.name()));

        return roleRepository.findAll()
                .stream()
                .filter(role -> isAdmin || !role.getName().equals(ROLES.ROLE_ADMIN.name()))
                .map(roleMapper::toDto)
                .toList();
    }

    // =========================
    // UPDATE
    // =========================
    @Override
    @Transactional
    public RoleDtoResponse updateRole(RoleDtoRequest dto, Long roleId) {
        Role role = findRole(roleId);

        if (roleRepository.existsByName(dto.name())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Role name already exists");
        }

        if (StringUtils.hasText(dto.name())) {
            role.setName(dto.name());
        }

        return roleMapper.toDto(roleRepository.save(role));
    }

    // =========================
    // DELETE
    // =========================
    @Override
    @Transactional
    public void deleteRole(Long roleId) {
        roleRepository.delete(findRole(roleId));
    }

    // =========================
    // HELPERS
    // =========================
    private Role findRole(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id.toString()));
    }
}