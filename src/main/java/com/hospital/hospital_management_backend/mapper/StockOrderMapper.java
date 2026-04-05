package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.StockOrderDtoRequest;
import com.hospital.hospital_management_backend.dto.response.StockOrderDtoResponse;
import com.hospital.hospital_management_backend.entity.DeliveryNote;
import com.hospital.hospital_management_backend.entity.StockOrder;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for converting between StockOrder entity and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StockOrderMapper {

    // REQUEST → ENTITY
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "supplier", ignore = true)      // resolved in service via supplierId
    @Mapping(target = "deliveryNotes", ignore = true) // handled in service
    StockOrder toEntity(StockOrderDtoRequest dto);

    // ENTITY → RESPONSE
    @Mapping(target = "supplier.id", source = "supplier.id")
    @Mapping(target = "supplier.name", source = "supplier.name")
    @Mapping(target = "deliveryNotes", source = "deliveryNotes")
    StockOrderDtoResponse toDto(StockOrder stockOrder);

    // DeliveryNote → DeliveryNoteDto
    @Mapping(target = "product.id", source = "product.id")
    @Mapping(target = "product.name", source = "product.name")
    StockOrderDtoResponse.DeliveryNoteDto toDeliveryNoteDto(DeliveryNote deliveryNote);

    // LIST
    List<StockOrderDtoResponse> toDtoList(List<StockOrder> stockOrders);

    // PATCH UPDATE
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "deliveryNotes", ignore = true)
    void updateFromDto(StockOrderDtoRequest dto, @MappingTarget StockOrder stockOrder);
}