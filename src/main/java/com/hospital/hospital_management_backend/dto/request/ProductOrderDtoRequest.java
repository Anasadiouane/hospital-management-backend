package com.hospital.hospital_management_backend.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

public record ProductOrderDtoRequest(

        @NotNull(message = "Order date is required.")
        LocalDateTime orderDate,

        @NotNull(message = "Delivery notes are required.")
        @Size(min = 1, message = "Order should have at least one delivery note.")
        Set<@Valid DeliveryNoteDto> deliveryNotes,

        @NotBlank(message = "Hospital service should not be blank.")
        String hospitalServiceName

) implements Serializable {}
