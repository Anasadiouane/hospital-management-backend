package com.hospital.hospital_management_backend.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;


public record MedicationDtoResponse(Long id, String name, String description, StockDto stock, String manufacturer,
                                    BigDecimal price) implements Serializable {

    public record StockDto(Long id, Long quantity, LocationDto location) implements Serializable {

        public record LocationDto(Long id, String name) implements Serializable {
        }
    }
}