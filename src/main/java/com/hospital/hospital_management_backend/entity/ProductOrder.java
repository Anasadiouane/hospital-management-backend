package com.hospital.hospital_management_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOrder extends Order {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private HospitalService hospitalService;
}