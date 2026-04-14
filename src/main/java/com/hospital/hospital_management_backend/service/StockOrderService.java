package com.hospital.hospital_management_backend.service;

import com.hospital.hospital_management_backend.dto.request.StockOrderDtoRequest;
import com.hospital.hospital_management_backend.dto.response.StockOrderDtoResponse;

import java.util.List;

public interface StockOrderService {
    StockOrderDtoResponse createStockOrder(StockOrderDtoRequest stockOrderDtoRequest);
    StockOrderDtoResponse getStockOrder(Long code);
    List<StockOrderDtoResponse> getAllStockOrder();
    List<StockOrderDtoResponse> getStockOrdersOfProduct(Long id);
    StockOrderDtoResponse updateStockOrder(Long code, StockOrderDtoRequest stockOrderDtoRequest);
    void deleteStockOrder(Long code);
}