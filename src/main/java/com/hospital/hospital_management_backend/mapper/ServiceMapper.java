package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.ServiceDtoRequest;
import com.hospital.hospital_management_backend.dto.response.ServiceDtoResponse;
import com.hospital.hospital_management_backend.entity.HospitalService;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting between HospitalService entity and DTOs.
 */
@Mapper(componentModel = "spring")
public interface ServiceMapper {

    HospitalService serviceDtoRequestToService(ServiceDtoRequest serviceDtoRequest);

    ServiceDtoResponse serviceToServiceDtoResponse(HospitalService hospitalService);
}