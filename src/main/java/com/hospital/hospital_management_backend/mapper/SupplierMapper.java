package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.SupplierDtoRequest;
import com.hospital.hospital_management_backend.dto.response.SupplierDtoResponse;
import com.hospital.hospital_management_backend.entity.Supplier;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting between Supplier entity and DTOs.
 */
@Mapper(componentModel = "spring")
public interface SupplierMapper {

    Supplier supplierDtoRequestToSupplier(SupplierDtoRequest supplierDtoRequest);

    SupplierDtoResponse supplierToSupplierDtoResponse(Supplier supplier);
}