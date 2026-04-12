package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.FileDtoRequest;
import com.hospital.hospital_management_backend.dto.response.FileDtoResponse;
import com.hospital.hospital_management_backend.entity.File;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for converting File entity to FileDtoResponse.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileMapper {

    // Entity → Response DTO
    FileDtoResponse toDto(File file);

    List<FileDtoResponse> toDtoList(List<File> files);

    // Request DTO → Entity
    @Mapping(target = "id", ignore = true)    // DB generated
    @Mapping(target = "name", ignore = true)  // set in service from multipart file
    @Mapping(target = "data", ignore = true)  // set in service from multipart file
    File toEntity(FileDtoRequest dto);

    // UPDATE (PATCH style)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)  // not in request
    @Mapping(target = "data", ignore = true)  // uploaded separately
    void updateFileFromDto(FileDtoRequest dto, @MappingTarget File file);
}