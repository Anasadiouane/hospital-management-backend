package com.hospital.hospital_management_backend.mapper;

import com.hospital.hospital_management_backend.dto.request.UserDtoRequest;
import com.hospital.hospital_management_backend.dto.response.UserDtoResponse;
import com.hospital.hospital_management_backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting between User entity and DTOs.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "imageId", source = "image.id")
    UserDtoResponse userToUserDtoResponse(User user);

    User userDtoRequestToUser(UserDtoRequest userDtoRequest);
}