package com.hospital.hospital_management_backend.dto.response;


import java.io.Serializable;


public record ServiceDtoResponse(Long id, String name) implements Serializable {
}