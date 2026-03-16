package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.ProductOrderDtoRequest;
import com.hospital.hospital_management_backend.dto.response.ProductOrderDtoResponse;
import com.hospital.hospital_management_backend.entity.ProductOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting between ProductOrder entity and DTOs.
 */
@Mapper(componentModel = "spring")
public interface ProductOrderMapper {

    ProductOrder toEntity(ProductOrderDtoRequest request);

    @Mapping(target = "hospitalServiceName", source = "hospitalService.name")
    ProductOrderDtoResponse toDto(ProductOrder productOrder);
}