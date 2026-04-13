package com.hospital.hospital_management_backend.service;

import com.hospital.hospital_management_backend.dto.request.ProductOrderDtoRequest;
import com.hospital.hospital_management_backend.dto.response.ProductOrderDtoResponse;

import java.util.List;

public interface ProductOrderService {
    ProductOrderDtoResponse createProductOrder(ProductOrderDtoRequest productOrderDtoRequest);

    ProductOrderDtoResponse getProductOrder(Long code);

    List<ProductOrderDtoResponse> getAllProductOrder();

    List<ProductOrderDtoResponse> getProductOrdersOfProduct(Long productId);

    ProductOrderDtoResponse updateProductOrder(Long code, ProductOrderDtoRequest productOrderDtoRequest);

    void deleteProductOrder(Long code);
}