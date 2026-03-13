package com.hospital.hospital_management_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "diplomas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diploma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Diploma title is required")
    @Column(name = "title", nullable = false)
    private String title;

    @PastOrPresent(message = "Start date must be in the past or present")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @PastOrPresent(message = "End date must be in the past or present")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "document_id")
    private File document;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;
}