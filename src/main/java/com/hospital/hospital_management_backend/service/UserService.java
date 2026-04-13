package com.hospital.hospital_management_backend.service;

import com.hospital.hospital_management_backend.dto.request.ChangePasswordDto;
import com.hospital.hospital_management_backend.dto.request.UpdateUserPasswordDto;
import com.hospital.hospital_management_backend.dto.request.UserDtoRequest;
import com.hospital.hospital_management_backend.dto.request.UserUpdateDtoRequest;
import com.hospital.hospital_management_backend.dto.response.UserDtoResponse;

import java.util.List;

public interface UserService {
    UserDtoResponse createUser(UserDtoRequest userDtoRequest);
    UserDtoResponse getUserById(Long userId);
    UserDtoResponse getUserByCin(String cin);
    UserDtoResponse getUserByEmail(String email);
    List<UserDtoResponse> getAllUsers();
    UserDtoResponse updateUser(UserUpdateDtoRequest userDtoRequest, Long userId);
    String updatePassword(String cin, UpdateUserPasswordDto updateUserPasswordDto);
    UserDtoResponse updatePassword(ChangePasswordDto passwordDto, Long userId);
    UserDtoResponse addImageToUser(Long userId, Long imageId);
    void deleteUser(Long userId);
}