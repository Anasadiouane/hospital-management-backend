package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.ServiceDtoRequest;
import com.hospital.hospital_management_backend.dto.response.ServiceDtoResponse;
import com.hospital.hospital_management_backend.entity.HospitalService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for converting between HospitalService entity and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceMapper {

    // REQUEST → ENTITY
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productOrders", ignore = true)  // relation managed separately
    HospitalService toEntity(ServiceDtoRequest dto);

    // ENTITY → RESPONSE
    ServiceDtoResponse toDto(HospitalService hospitalService);

    // LIST
    List<ServiceDtoResponse> toDtoList(List<HospitalService> services);
}