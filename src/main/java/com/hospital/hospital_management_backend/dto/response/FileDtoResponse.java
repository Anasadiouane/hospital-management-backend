package com.hospital.hospital_management_backend.dto.response;

import java.io.Serializable;


public record FileDtoResponse(Long id, String name, String type, long size) implements Serializable {
}