package com.hospital.hospital_management_backend.service.impl;

import com.hospital.hospital_management_backend.dto.request.LocationDtoRequest;
import com.hospital.hospital_management_backend.dto.response.LocationDtoResponse;
import com.hospital.hospital_management_backend.entity.Location;
import com.hospital.hospital_management_backend.exception.ResourceNotFoundException;
import com.hospital.hospital_management_backend.mapper.LocationMapper;
import com.hospital.hospital_management_backend.repository.LocationRepository;
import com.hospital.hospital_management_backend.service.LocationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service implementation for managing locations.
 */
@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    public LocationServiceImpl(LocationRepository locationRepository,
                               LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    // =========================
    // CREATE
    // =========================
    @Override
    @Transactional
    public LocationDtoResponse createLocation(LocationDtoRequest dto) {
        Location location = locationMapper.toEntity(dto);
        return locationMapper.toDto(locationRepository.save(location));
    }

    // =========================
    // READ BY ID
    // =========================
    @Override
    @Transactional(readOnly = true)
    public LocationDtoResponse getById(Long id) {
        return locationMapper.toDto(findLocation(id));
    }

    // =========================
    // GET ALL
    // =========================
    @Override
    @Transactional(readOnly = true)
    public List<LocationDtoResponse> getAllLocation() {
        return locationRepository.findAll()
                .stream()
                .map(locationMapper::toDto)
                .toList();
    }

    // =========================
    // UPDATE
    // =========================
    @Override
    @Transactional
    public LocationDtoResponse updateLocation(Long id, LocationDtoRequest dto) {
        Location location = findLocation(id);
        locationMapper.updateLocationFromDto(dto, location);
        return locationMapper.toDto(locationRepository.save(location));
    }

    // =========================
    // DELETE
    // =========================
    @Override
    @Transactional
    public void deleteLocation(Long id) {
        locationRepository.delete(findLocation(id));
    }

    // =========================
    // HELPERS
    // =========================
    private Location findLocation(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location", "id", id.toString()));
    }
}