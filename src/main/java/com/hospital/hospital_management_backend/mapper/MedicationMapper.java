package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.MedicationDtoRequest;
import com.hospital.hospital_management_backend.dto.response.MedicationDtoResponse;
import com.hospital.hospital_management_backend.entity.Medication;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting between Medication entity and DTOs.
 */
@Mapper(componentModel = "spring")
public interface MedicationMapper {

    Medication medicationDtoRequestToMedication(MedicationDtoRequest request);

    MedicationDtoResponse medicationToMedicationDtoResponse(Medication medication);
}