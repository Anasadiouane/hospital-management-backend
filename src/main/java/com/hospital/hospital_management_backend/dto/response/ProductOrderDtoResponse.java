package com.hospital.hospital_management_backend.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;


public record ProductOrderDtoResponse(String code, LocalDateTime orderDate, Set<DeliveryNoteDto> deliveryNotes,
                                      String hospitalServiceName) implements Serializable {

    public record DeliveryNoteDto(Long id, ProductDto product, Long quantity) implements Serializable {

        public record ProductDto(Long id, String name) implements Serializable {
        }
    }
}