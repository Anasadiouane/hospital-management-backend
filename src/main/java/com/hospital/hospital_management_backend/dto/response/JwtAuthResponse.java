package com.hospital.hospital_management_backend.dto.response;

import com.hospital.hospital_management_backend.enums.TokenType;

public record JwtAuthResponse (String token, TokenType type){
}