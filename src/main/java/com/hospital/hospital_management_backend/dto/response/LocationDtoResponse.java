package com.hospital.hospital_management_backend.dto.response;

import java.io.Serializable;


public record LocationDtoResponse(Long id, String name, String description, String address, String latitude,
                                  String longitude) implements Serializable {
}