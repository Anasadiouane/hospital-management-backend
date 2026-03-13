package com.hospital.hospital_management_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "medications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medication extends Product {

    @NotBlank(message = "Manufacturer is required")
    @Column(name = "manufacturer", nullable = false)
    private String manufacturer;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be non-negative")
    @Column(name = "price", nullable = false)
    private BigDecimal price;
}