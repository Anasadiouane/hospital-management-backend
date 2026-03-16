package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.LocationDtoRequest;
import com.hospital.hospital_management_backend.dto.response.LocationDtoResponse;
import com.hospital.hospital_management_backend.entity.Location;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting between Location entity and DTOs.
 */
@Mapper(componentModel = "spring")
public interface LocationMapper {

    Location locationDtoRequestToLocation(LocationDtoRequest locationDtoRequest);

    LocationDtoResponse locationToLocationDtoResponse(Location location);
}