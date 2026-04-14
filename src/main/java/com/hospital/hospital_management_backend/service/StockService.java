package com.hospital.hospital_management_backend.service;

import com.hospital.hospital_management_backend.dto.request.StockDtoRequest;
import com.hospital.hospital_management_backend.dto.request.StockUpdateDtoRequest;
import com.hospital.hospital_management_backend.dto.response.StockDtoResponse;

import java.util.List;

public interface StockService {
    StockDtoResponse createStock(StockDtoRequest stockDtoRequest);
    StockDtoResponse getById(Long stockId);
    StockDtoResponse getStockByProduct(Long productId);
    List<StockDtoResponse> getAllStock();
    StockDtoResponse updateStock(Long stockId, StockUpdateDtoRequest stockUpdateDtoRequest);
    StockDtoResponse addLocationToStock(Long stockId, Long locationId);
    void deleteStock(Long stockId);
}