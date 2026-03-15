package com.hospital.hospital_management_backend.dto.response;

import java.io.Serializable;


public record StockDtoResponse(Long id, Long quantity, ProductDto product,
                               LocationDto location) implements Serializable {

    public record ProductDto(Long id, String name) implements Serializable {
    }

    public record LocationDto(Long id, String name) implements Serializable {
    }
}