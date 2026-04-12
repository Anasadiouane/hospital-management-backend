package com.hospital.hospital_management_backend.service;

import com.hospital.hospital_management_backend.dto.request.LocationDtoRequest;
import com.hospital.hospital_management_backend.dto.response.LocationDtoResponse;

import java.util.List;

public interface LocationService {
    LocationDtoResponse createLocation(LocationDtoRequest locationDtoRequest);
    LocationDtoResponse getById(Long locationId);
    List<LocationDtoResponse> getAllLocation();
    LocationDtoResponse updateLocation(Long locationId, LocationDtoRequest locationDtoRequest);
    void deleteLocation(Long locationId);
}