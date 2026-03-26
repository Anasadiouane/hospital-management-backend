package com.hospital.hospital_management_backend.service;

import com.hospital.hospital_management_backend.dto.request.LoginRequestDto;

public interface AuthService {
    String login(LoginRequestDto loginDto);
}