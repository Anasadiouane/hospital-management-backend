package com.hospital.hospital_management_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vacations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vacation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be in the present or future")
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date must be in the present or future")
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "is_sick_vacation", nullable = false)
    private boolean sickVacation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
}
