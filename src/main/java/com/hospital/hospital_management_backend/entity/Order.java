package com.hospital.hospital_management_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "orders")
@Inheritance(strategy = InheritanceType.JOINED) // more efficient than TABLE_PER_CLASS
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // consistent naming

    @NotNull(message = "Order date is required")
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<DeliveryNote> deliveryNotes;


}