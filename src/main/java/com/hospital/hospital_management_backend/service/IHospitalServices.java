package com.hospital.hospital_management_backend.service;

import com.hospital.hospital_management_backend.dto.request.ServiceDtoRequest;
import com.hospital.hospital_management_backend.dto.response.ServiceDtoResponse;

import java.util.List;

public interface IHospitalServices {
    ServiceDtoResponse createService(ServiceDtoRequest serviceDto);
    ServiceDtoResponse getServiceById(Long serviceId);
    ServiceDtoResponse getServiceByName(String serviceName);
    List<ServiceDtoResponse> getAllServices();
    ServiceDtoResponse updateService(ServiceDtoRequest serviceDto, Long serviceId);
    void deleteService(Long serviceId);
}