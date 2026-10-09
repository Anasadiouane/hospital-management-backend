package com.hospital.hospital_management_backend.controller;

import com.hospital.hospital_management_backend.dto.request.RoleDtoRequest;
import com.hospital.hospital_management_backend.dto.response.RoleDtoResponse;
import com.hospital.hospital_management_backend.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<RoleDtoResponse> createRole(@Valid @RequestBody RoleDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(dto));
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<RoleDtoResponse> getRoleById(@PathVariable Long roleId) {
        return ResponseEntity.ok(roleService.getRoleById(roleId));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<RoleDtoResponse> getRoleByName(@PathVariable String name) {
        return ResponseEntity.ok(roleService.getRoleByName(name));
    }

    @GetMapping
    public ResponseEntity<List<RoleDtoResponse>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<RoleDtoResponse> updateRole(@PathVariable Long roleId,
                                                      @Valid @RequestBody RoleDtoRequest dto) {
        return ResponseEntity.ok(roleService.updateRole(dto, roleId));
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }
}
