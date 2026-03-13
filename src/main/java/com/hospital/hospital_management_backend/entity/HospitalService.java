package com.hospital.hospital_management_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Entity
@Table(
        name = "hospital_services",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Service name is required")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "hospitalService", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ProductOrder> productOrders;
}