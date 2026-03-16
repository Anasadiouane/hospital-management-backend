package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.StockDtoRequest;
import com.hospital.hospital_management_backend.dto.response.StockDtoResponse;
import com.hospital.hospital_management_backend.entity.Stock;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting between Stock entity and DTOs.
 */
@Mapper(componentModel = "spring")
public interface StockMapper {

    Stock toEntity(StockDtoRequest stockDtoRequest);

    StockDtoResponse toDto(Stock stock);
}