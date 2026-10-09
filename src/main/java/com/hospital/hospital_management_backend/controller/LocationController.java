package com.hospital.hospital_management_backend.controller;

import com.hospital.hospital_management_backend.dto.request.LocationDtoRequest;
import com.hospital.hospital_management_backend.dto.response.LocationDtoResponse;
import com.hospital.hospital_management_backend.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public ResponseEntity<LocationDtoResponse> createLocation(@Valid @RequestBody LocationDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(locationService.createLocation(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDtoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(locationService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<LocationDtoResponse>> getAllLocation() {
        return ResponseEntity.ok(locationService.getAllLocation());
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDtoResponse> updateLocation(@PathVariable Long id,
                                                              @Valid @RequestBody LocationDtoRequest dto) {
        return ResponseEntity.ok(locationService.updateLocation(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
