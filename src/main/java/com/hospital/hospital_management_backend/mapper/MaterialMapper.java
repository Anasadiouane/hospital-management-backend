package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.MaterialDtoRequest;
import com.hospital.hospital_management_backend.dto.response.MaterialDtoResponse;
import com.hospital.hospital_management_backend.dto.response.MaterialCreateDtoResponse;
import com.hospital.hospital_management_backend.entity.Material;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting between Material entity and DTOs.
 */
@Mapper(componentModel = "spring")
public interface MaterialMapper {

    Material materialDtoRequestToMaterial(MaterialDtoRequest materialDtoRequest);

    MaterialDtoResponse materialToMaterialDtoResponse(Material material);

    MaterialCreateDtoResponse materialToMaterialCreateDtoResponse(Material material);
}