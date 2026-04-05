package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.MedicationDtoRequest;
import com.hospital.hospital_management_backend.dto.response.MedicationDtoResponse;
import com.hospital.hospital_management_backend.entity.Medication;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for converting between Medication entity and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MedicationMapper {

    // ENTITY → RESPONSE
    @Mapping(target = "stock.id", source = "stock.id")
    @Mapping(target = "stock.quantity", source = "stock.quantity")
    @Mapping(target = "stock.location.id", source = "stock.location.id")
    @Mapping(target = "stock.location.name", source = "stock.location.name")
    MedicationDtoResponse toDto(Medication medication);

    List<MedicationDtoResponse> toDtoList(List<Medication> medications);

    // REQUEST → ENTITY
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stock", ignore = true)         // created in service
    @Mapping(target = "deliveryNotes", ignore = true) // relation managed separately
    Medication toEntity(MedicationDtoRequest dto);

    // PATCH UPDATE
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stock", ignore = true)
    @Mapping(target = "deliveryNotes", ignore = true)
    void updateFromDto(MedicationDtoRequest dto, @MappingTarget Medication medication);
}