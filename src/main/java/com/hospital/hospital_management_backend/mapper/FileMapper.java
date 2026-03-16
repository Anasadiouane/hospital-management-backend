package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.response.FileDtoResponse;
import com.hospital.hospital_management_backend.entity.File;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting File entity to FileDtoResponse.
 */
@Mapper(componentModel = "spring")
public interface FileMapper {

    FileDtoResponse fileToFileDtoResponse(File file);
}