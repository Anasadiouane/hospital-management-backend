package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.VacationDtoRequest;
import com.hospital.hospital_management_backend.dto.response.VacationDtoResponse;
import com.hospital.hospital_management_backend.entity.Vacation;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for converting between Vacation entity and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VacationMapper {

    // REQUEST → ENTITY
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", ignore = true)      // resolved in service
    @Mapping(target = "sickVacation", source = "isSickVacation")
    Vacation toEntity(VacationDtoRequest dto);

    // ENTITY → RESPONSE
    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "isSickVacation", source = "sickVacation")
    VacationDtoResponse toDto(Vacation vacation);

    // LIST
    List<VacationDtoResponse> toDtoList(List<Vacation> vacations);

    // PATCH UPDATE
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "sickVacation", source = "isSickVacation")
    void updateFromDto(VacationDtoRequest dto, @MappingTarget Vacation vacation);
}