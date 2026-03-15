package com.hospital.hospital_management_backend.dto.response;

import java.util.List;

public record PageDtoResponse(
        List<?> contents,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last
) {
}