package com.hospital.hospital_management_backend.controller;

import com.hospital.hospital_management_backend.dto.request.ServiceDtoRequest;
import com.hospital.hospital_management_backend.dto.response.ServiceDtoResponse;
import com.hospital.hospital_management_backend.service.IHospitalServices;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/services")
public class HospitalServiceController {

    private final IHospitalServices hospitalServices;

    public HospitalServiceController(IHospitalServices hospitalServices) {
        this.hospitalServices = hospitalServices;
    }

    @PostMapping
    public ResponseEntity<ServiceDtoResponse> createService(@Valid @RequestBody ServiceDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hospitalServices.createService(dto));
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceDtoResponse> getServiceById(@PathVariable Long serviceId) {
        return ResponseEntity.ok(hospitalServices.getServiceById(serviceId));
    }

    @GetMapping("/name/{serviceName}")
    public ResponseEntity<ServiceDtoResponse> getServiceByName(@PathVariable String serviceName) {
        return ResponseEntity.ok(hospitalServices.getServiceByName(serviceName));
    }

    @GetMapping
    public ResponseEntity<List<ServiceDtoResponse>> getAllServices() {
        return ResponseEntity.ok(hospitalServices.getAllServices());
    }

    @PutMapping("/{serviceId}")
    public ResponseEntity<ServiceDtoResponse> updateService(@PathVariable Long serviceId,
                                                            @Valid @RequestBody ServiceDtoRequest dto) {
        return ResponseEntity.ok(hospitalServices.updateService(dto, serviceId));
    }

    @DeleteMapping("/{serviceId}")
    public ResponseEntity<Void> deleteService(@PathVariable Long serviceId) {
        hospitalServices.deleteService(serviceId);
        return ResponseEntity.noContent().build();
    }
}
