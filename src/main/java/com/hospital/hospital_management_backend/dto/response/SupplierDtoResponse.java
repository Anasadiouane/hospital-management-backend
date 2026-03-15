package com.hospital.hospital_management_backend.dto.response;

import java.io.Serializable;


public record SupplierDtoResponse(Long id, String name, String address, String phone,
                                  String email) implements Serializable {
}