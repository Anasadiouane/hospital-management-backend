package com.hospital.hospital_management_backend.service.impl;

import com.hospital.hospital_management_backend.dto.request.ServiceDtoRequest;
import com.hospital.hospital_management_backend.dto.response.ServiceDtoResponse;
import com.hospital.hospital_management_backend.entity.HospitalService;
import com.hospital.hospital_management_backend.exception.AppException;
import com.hospital.hospital_management_backend.exception.ResourceNotFoundException;
import com.hospital.hospital_management_backend.mapper.ServiceMapper;
import com.hospital.hospital_management_backend.repository.ServiceRepository;
import com.hospital.hospital_management_backend.service.IHospitalServices;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class IHospitalServicesImpl implements IHospitalServices {

    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;

    public IHospitalServicesImpl(ServiceRepository serviceRepository,
                                 ServiceMapper serviceMapper) {
        this.serviceRepository = serviceRepository;
        this.serviceMapper = serviceMapper;
    }

    @Override
    @Transactional
    public ServiceDtoResponse createService(ServiceDtoRequest dto) {
        if (serviceRepository.existsByName(dto.name())) {
            throw new AppException(HttpStatus.CONFLICT, "Service already exists");
        }
        return serviceMapper.toDto(serviceRepository.save(serviceMapper.toEntity(dto)));
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceDtoResponse getServiceById(Long serviceId) {
        return serviceMapper.toDto(findService(serviceId));
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceDtoResponse getServiceByName(String serviceName) {
        return serviceMapper.toDto(
                serviceRepository.findByName(serviceName)
                        .orElseThrow(() -> new ResourceNotFoundException("Service", "name", serviceName))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceDtoResponse> getAllServices() {
        return serviceMapper.toDtoList(serviceRepository.findAll());
    }

    @Override
    @Transactional
    public ServiceDtoResponse updateService(ServiceDtoRequest dto, Long serviceId) {
        HospitalService hospitalService = findService(serviceId);

        // only check uniqueness when the name really changes
        if (StringUtils.hasText(dto.name()) && !dto.name().equals(hospitalService.getName())) {
            if (serviceRepository.existsByName(dto.name())) {
                throw new AppException(HttpStatus.CONFLICT, "Service name already exists");
            }
            hospitalService.setName(dto.name());
        }

        return serviceMapper.toDto(serviceRepository.save(hospitalService));
    }

    @Override
    @Transactional
    public void deleteService(Long serviceId) {
        serviceRepository.delete(findService(serviceId));
    }

    private HospitalService findService(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", id.toString()));
    }
}
