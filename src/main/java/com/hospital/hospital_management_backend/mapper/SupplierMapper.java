package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.SupplierDtoRequest;
import com.hospital.hospital_management_backend.dto.response.SupplierDtoResponse;
import com.hospital.hospital_management_backend.entity.Supplier;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for converting between Supplier entity and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SupplierMapper {

    // REQUEST → ENTITY
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stockOrders", ignore = true)  // relation managed separately
    Supplier toEntity(SupplierDtoRequest dto);

    // ENTITY → RESPONSE
    SupplierDtoResponse toDto(Supplier supplier);

    // LIST
    List<SupplierDtoResponse> toDtoList(List<Supplier> suppliers);

    // PATCH UPDATE
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stockOrders", ignore = true)
    void updateFromDto(SupplierDtoRequest dto, @MappingTarget Supplier supplier);
}