package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.EmployeeDtoRequest;
import com.hospital.hospital_management_backend.dto.response.EmployeeDtoResponse;
import com.hospital.hospital_management_backend.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting between Employee entity and DTOs.
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "serviceName", source = "service.name")
    EmployeeDtoResponse employeeToEmployeeDtoResponse(Employee employee);

    @Mapping(target = "service.name", source = "serviceName")
    Employee employeeDtoRequestToEmployee(EmployeeDtoRequest employeeDtoRequest);
}