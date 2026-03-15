package com.hospital.hospital_management_backend.dto.response;

import java.io.Serializable;


public record UserDtoResponse(Long id, String firstName, String lastName,
                              String email, String cin,
                              RoleDtoResponse role, Long imageId) implements Serializable {
}