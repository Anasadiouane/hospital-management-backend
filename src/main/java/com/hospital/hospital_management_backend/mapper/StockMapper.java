package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.StockDtoRequest;
import com.hospital.hospital_management_backend.dto.response.StockDtoResponse;
import com.hospital.hospital_management_backend.entity.Stock;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for converting between Stock entity and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StockMapper {

    // REQUEST → ENTITY
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)    // resolved in service via productId
    @Mapping(target = "location", ignore = true)   // resolved in service
    Stock toEntity(StockDtoRequest dto);

    // ENTITY → RESPONSE
    @Mapping(target = "product.id", source = "product.id")
    @Mapping(target = "product.name", source = "product.name")
    @Mapping(target = "location.id", source = "location.id")
    @Mapping(target = "location.name", source = "location.name")
    StockDtoResponse toDto(Stock stock);

    // LIST
    List<StockDtoResponse> toDtoList(List<Stock> stocks);

    // PATCH UPDATE
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "location", ignore = true)
    void updateStockFromDto(StockDtoRequest dto, @MappingTarget Stock stock);
}