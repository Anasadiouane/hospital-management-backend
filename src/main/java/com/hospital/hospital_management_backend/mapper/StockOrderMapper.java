package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.StockOrderDtoRequest;
import com.hospital.hospital_management_backend.dto.response.StockOrderDtoResponse;
import com.hospital.hospital_management_backend.entity.StockOrder;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting between StockOrder entity and DTOs.
 */
@Mapper(componentModel = "spring")
public interface StockOrderMapper {

    StockOrder toEntity(StockOrderDtoRequest request);

    StockOrderDtoResponse toDto(StockOrder stockOrder);
}