package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.VacationDtoRequest;
import com.hospital.hospital_management_backend.dto.response.VacationDtoResponse;
import com.hospital.hospital_management_backend.entity.Vacation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting between Vacation entity and DTOs.
 */
@Mapper(componentModel = "spring")
public interface VacationMapper {

    @Mapping(target = "sickVacation", source = "isSickVacation")
    Vacation vacationDtoRequestToVacation(VacationDtoRequest vacationDtoRequest);

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "isSickVacation", source = "sickVacation")
    VacationDtoResponse vacationToVacationDtoResponse(Vacation vacation);
}