package com.hospital.hospital_management_backend.service;

import com.hospital.hospital_management_backend.dto.request.SupplierDtoRequest;
import com.hospital.hospital_management_backend.dto.response.StockOrderDtoResponse;
import com.hospital.hospital_management_backend.dto.response.SupplierDtoResponse;

import java.util.List;

public interface SupplierService {
    SupplierDtoResponse createSupplier(SupplierDtoRequest supplierDtoRequest);
    SupplierDtoResponse getById(Long supplierId);
    List<SupplierDtoResponse> getAllSupplier();
    List<StockOrderDtoResponse> getStockOrdersForSupplier(Long supplierId);
    SupplierDtoResponse updateSupplier(Long supplierId, SupplierDtoRequest supplierDtoRequest);
    void deleteSupplier(Long supplierId);
}