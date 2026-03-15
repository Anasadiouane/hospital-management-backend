package com.hospital.hospital_management_backend.dto.response;

import java.io.Serializable;


public record MaterialCreateDtoResponse(Long id, String name, String description) implements Serializable {
}