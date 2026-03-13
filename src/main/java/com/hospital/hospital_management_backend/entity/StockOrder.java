package com.hospital.hospital_management_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "stock_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockOrder extends Order {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @NotBlank(message = "Delivery location is required")
    @Column(name = "delivery_location", nullable = false)
    private String deliveryLocation;
}