package com.hospital.hospital_management_backend.repository;

import com.hospital.hospital_management_backend.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByCin(String cin);
    Optional<Employee> findByRegistrationNumber(String registrationNumber);
    Page<Employee> findAllByHospitalServiceId(Long id, Pageable pageable);
    Page<Employee> findAllByCinIsLikeIgnoreCase(String cin, Pageable pageable);
    Page<Employee> findAllByFirstNameIsLikeIgnoreCase(String firstName, Pageable pageable);
    Page<Employee> findAllByLastNameIsIgnoreCase(String lastName, Pageable pageable);
    Page<Employee> findAllByRegistrationNumberIsLikeIgnoreCase(String registrationNumber, Pageable pageable);
    Page<Employee> findAllByRecruitmentDate(LocalDate recruitmentDate, Pageable pageable);
}