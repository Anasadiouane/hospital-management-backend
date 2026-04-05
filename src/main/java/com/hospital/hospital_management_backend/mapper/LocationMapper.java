package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.LocationDtoRequest;
import com.hospital.hospital_management_backend.dto.response.LocationDtoResponse;
import com.hospital.hospital_management_backend.entity.Location;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for converting between Location entity and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocationMapper {

    // Entity → Response DTO
    LocationDtoResponse toDto(Location location);

    List<LocationDtoResponse> toDtoList(List<Location> locations);

    // Request DTO → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stocks", ignore = true)  // relation handled in service
    Location toEntity(LocationDtoRequest dto);

    // UPDATE (PATCH style)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stocks", ignore = true)  // relation handled in service
    void updateLocationFromDto(LocationDtoRequest dto, @MappingTarget Location location);
}