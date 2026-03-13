package com.hospital.hospital_management_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(
        name = "employees",
        uniqueConstraints = @UniqueConstraint(columnNames = {"cin", "registration_number"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "CIN is required")
    @Size(min = 6, max = 20, message = "CIN must be between 6 and 20 characters")
    @Column(name = "cin", nullable = false)
    private String cin;

    @NotBlank(message = "Registration number is required")
    @Column(name = "registration_number", nullable = false, unique = true)
    private String registrationNumber;

    @Column(name = "recruitment_date", nullable = false)
    private LocalDate recruitmentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private HospitalService hospitalService;

    @OneToMany(
            cascade = CascadeType.REMOVE,
            mappedBy = "employee",
            fetch = FetchType.LAZY
    )
    private Set<Vacation> vacations;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinTable(
            name = "employee_documents",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    private Set<File> documents;

    @OneToMany(
            cascade = CascadeType.REMOVE,
            mappedBy = "employee",
            fetch = FetchType.LAZY
    )
    private Set<Diploma> diplomas;
}