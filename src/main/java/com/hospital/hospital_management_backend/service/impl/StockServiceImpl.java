package com.hospital.hospital_management_backend.service.impl;

import com.hospital.hospital_management_backend.dto.request.StockDtoRequest;
import com.hospital.hospital_management_backend.dto.request.StockUpdateDtoRequest;
import com.hospital.hospital_management_backend.dto.response.StockDtoResponse;
import com.hospital.hospital_management_backend.entity.Location;
import com.hospital.hospital_management_backend.entity.Product;
import com.hospital.hospital_management_backend.entity.Stock;
import com.hospital.hospital_management_backend.exception.AppException;
import com.hospital.hospital_management_backend.exception.ResourceNotFoundException;
import com.hospital.hospital_management_backend.mapper.StockMapper;
import com.hospital.hospital_management_backend.repository.LocationRepository;
import com.hospital.hospital_management_backend.repository.ProductRepository;
import com.hospital.hospital_management_backend.repository.StockRepository;
import com.hospital.hospital_management_backend.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service implementation for managing stock.
 */
@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final LocationRepository locationRepository;
    private final StockMapper stockMapper;

    public StockServiceImpl(StockRepository stockRepository,
                            ProductRepository productRepository,
                            LocationRepository locationRepository,
                            StockMapper stockMapper) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
        this.locationRepository = locationRepository;
        this.stockMapper = stockMapper;
    }

    // =========================
    // CREATE
    // =========================
    @Override
    @Transactional
    public StockDtoResponse createStock(StockDtoRequest dto) {
        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", dto.productId().toString()));

        if (product.getStock() != null) {
            throw new AppException(HttpStatus.BAD_REQUEST,
                    String.format("Product with id %d already has a stock", product.getId()));
        }

        Stock stock = stockMapper.toEntity(dto);
        stock.setProduct(product);

        return stockMapper.toDto(stockRepository.save(stock));
    }

    // =========================
    // READ
    // =========================
    @Override
    @Transactional(readOnly = true)
    public StockDtoResponse getById(Long stockId) {
        return stockMapper.toDto(findStock(stockId));
    }

    @Override
    @Transactional(readOnly = true)
    public StockDtoResponse getStockByProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId.toString()));

        Stock stock = product.getStock();
        if (stock == null) {
            throw new AppException(HttpStatus.BAD_REQUEST,
                    String.format("Product with id %d doesn't have a stock", productId));
        }

        return stockMapper.toDto(stock);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockDtoResponse> getAllStock() {
        return stockMapper.toDtoList(stockRepository.findAll());
    }

    // =========================
    // UPDATE
    // =========================
    @Override
    @Transactional
    public StockDtoResponse updateStock(Long stockId, StockUpdateDtoRequest dto) {
        Stock stock = findStock(stockId);
        stock.setQuantity(dto.quantity());
        return stockMapper.toDto(stockRepository.save(stock));
    }

    // =========================
    // ADD LOCATION
    // =========================
    @Override
    @Transactional
    public StockDtoResponse addLocationToStock(Long stockId, Long locationId) {
        Stock stock = findStock(stockId);

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location", "id", locationId.toString()));

        stock.setLocation(location);

        return stockMapper.toDto(stockRepository.save(stock));
    }

    // =========================
    // DELETE
    // =========================
    @Override
    @Transactional
    public void deleteStock(Long stockId) {
        stockRepository.delete(findStock(stockId));
    }

    // =========================
    // HELPERS
    // =========================
    private Stock findStock(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock", "id", id.toString()));
    }
}