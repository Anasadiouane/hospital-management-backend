package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.ProductOrderDtoRequest;
import com.hospital.hospital_management_backend.dto.response.ProductOrderDtoResponse;
import com.hospital.hospital_management_backend.entity.DeliveryNote;
import com.hospital.hospital_management_backend.entity.ProductOrder;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for converting between ProductOrder entity and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductOrderMapper {

    // ENTITY → RESPONSE
    @Mapping(target = "hospitalServiceName", source = "hospitalService.name")
    @Mapping(target = "deliveryNotes", source = "deliveryNotes")
    ProductOrderDtoResponse toDto(ProductOrder order);

    List<ProductOrderDtoResponse> toDtoList(List<ProductOrder> orders);

    // DeliveryNote → DeliveryNoteDto
    @Mapping(target = "product.id", source = "product.id")
    @Mapping(target = "product.name", source = "product.name")
    ProductOrderDtoResponse.DeliveryNoteDto toDeliveryNoteDto(DeliveryNote deliveryNote);

    // REQUEST → ENTITY
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deliveryNotes", ignore = true)   // handled in service
    @Mapping(target = "hospitalService", ignore = true) // resolved in service
    ProductOrder toEntity(ProductOrderDtoRequest dto);

    // PATCH UPDATE
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deliveryNotes", ignore = true)
    @Mapping(target = "hospitalService", ignore = true)
    void updateFromDto(ProductOrderDtoRequest dto, @MappingTarget ProductOrder order);
}