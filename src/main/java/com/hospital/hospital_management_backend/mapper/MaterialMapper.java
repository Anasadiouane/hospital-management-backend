package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.MaterialDtoRequest;
import com.hospital.hospital_management_backend.dto.response.MaterialDtoResponse;
import com.hospital.hospital_management_backend.dto.response.MaterialCreateDtoResponse;
import com.hospital.hospital_management_backend.entity.Material;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for converting between Material entity and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MaterialMapper {

    // ENTITY → CREATE RESPONSE
    MaterialCreateDtoResponse toCreateDto(Material material);

    List<MaterialCreateDtoResponse> toCreateDtoList(List<Material> materials);

    // ENTITY → FULL RESPONSE
    @Mapping(target = "stock.id", source = "stock.id")
    @Mapping(target = "stock.quantity", source = "stock.quantity")
    @Mapping(target = "stock.location.id", source = "stock.location.id")
    @Mapping(target = "stock.location.name", source = "stock.location.name")
    MaterialDtoResponse toDto(Material material);

    List<MaterialDtoResponse> toDtoList(List<Material> materials);

    // REQUEST → ENTITY
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "qrCode", ignore = true)    // generated separately
    @Mapping(target = "stock", ignore = true)      // created separately in service
    @Mapping(target = "deliveryNotes", ignore = true)
    Material toEntity(MaterialDtoRequest dto);

    // UPDATE (PATCH STYLE)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "qrCode", ignore = true)
    @Mapping(target = "stock", ignore = true)
    @Mapping(target = "deliveryNotes", ignore = true)
    void updateMaterialFromDto(MaterialDtoRequest dto, @MappingTarget Material material);
}