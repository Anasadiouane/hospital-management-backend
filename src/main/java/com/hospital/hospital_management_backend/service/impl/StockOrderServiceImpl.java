package com.hospital.hospital_management_backend.service.impl;

import com.hospital.hospital_management_backend.dto.request.DeliveryNoteDto;
import com.hospital.hospital_management_backend.dto.request.StockOrderDtoRequest;
import com.hospital.hospital_management_backend.dto.response.StockOrderDtoResponse;
import com.hospital.hospital_management_backend.entity.*;
import com.hospital.hospital_management_backend.exception.AppException;
import com.hospital.hospital_management_backend.exception.ResourceNotFoundException;
import com.hospital.hospital_management_backend.mapper.StockOrderMapper;
import com.hospital.hospital_management_backend.repository.*;
import com.hospital.hospital_management_backend.service.StockOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service implementation for managing stock orders.
 */
@Service
public class StockOrderServiceImpl implements StockOrderService {

    private final StockOrderRepository stockOrderRepository;
    private final SupplierRepository supplierRepository;
    private final DeliveryNoteRepository deliveryNoteRepository;
    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final StockOrderMapper stockOrderMapper;

    public StockOrderServiceImpl(StockOrderRepository stockOrderRepository,
                                 SupplierRepository supplierRepository,
                                 DeliveryNoteRepository deliveryNoteRepository,
                                 StockRepository stockRepository,
                                 ProductRepository productRepository,
                                 StockOrderMapper stockOrderMapper) {
        this.stockOrderRepository = stockOrderRepository;
        this.supplierRepository = supplierRepository;
        this.deliveryNoteRepository = deliveryNoteRepository;
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
        this.stockOrderMapper = stockOrderMapper;
    }

    @Override
    @Transactional
    public StockOrderDtoResponse createStockOrder(StockOrderDtoRequest stockOrderDtoRequest) {
        StockOrder stockOrder = stockOrderMapper.toEntity(stockOrderDtoRequest);

        Supplier supplier = supplierRepository.findById(stockOrderDtoRequest.supplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", stockOrderDtoRequest.supplierId().toString()));
        stockOrder.setSupplier(supplier);

        StockOrder savedStockOrder = stockOrderRepository.save(stockOrder);

        Set<DeliveryNoteDto> deliveryNotes = stockOrderDtoRequest.deliveryNotes();
        deliveryNotes.forEach(dn -> {
            ProductStockWrapper wrapper = getValidatedProductAndStock(dn.productId());
            Product product = wrapper.product();
            Stock stock = wrapper.stock();

            stock.setQuantity(stock.getQuantity() + dn.quantity());
            stockRepository.save(stock);

            DeliveryNote deliveryNote = new DeliveryNote();
            deliveryNote.setProduct(product);
            deliveryNote.setQuantity(dn.quantity());
            deliveryNote.setOrder(savedStockOrder);
            deliveryNoteRepository.save(deliveryNote);
        });

        StockOrder order = stockOrderRepository.save(savedStockOrder);
        return stockOrderMapper.toDto(order);
    }

    @Override
    public StockOrderDtoResponse getStockOrder(Long code) {
        StockOrder stockOrder = stockOrderRepository.findById(code)
                .orElseThrow(() -> new ResourceNotFoundException("Stock Order", "code", code.toString()));
        return stockOrderMapper.toDto(stockOrder);
    }

    @Override
    public List<StockOrderDtoResponse> getAllStockOrder() {
        return stockOrderRepository.findAll().stream()
                .map(stockOrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockOrderDtoResponse> getStockOrdersOfProduct(Long id) {
        productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id.toString()));

        return stockOrderRepository.findAllByProduct(id).stream()
                .peek(so -> so.setDeliveryNotes(so.getDeliveryNotes().stream()
                        .filter(dn -> dn.getProduct().getId().equals(id))
                        .collect(Collectors.toSet())))
                .map(stockOrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StockOrderDtoResponse updateStockOrder(Long code, StockOrderDtoRequest stockOrderDtoRequest) {
        StockOrder stockOrder = stockOrderRepository.findById(code)
                .orElseThrow(() -> new ResourceNotFoundException("Stock order", "code", code.toString()));

        if (StringUtils.hasText(stockOrderDtoRequest.deliveryLocation())) {
            stockOrder.setDeliveryLocation(stockOrderDtoRequest.deliveryLocation());
        }

        Supplier supplier = supplierRepository.findById(stockOrderDtoRequest.supplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", stockOrderDtoRequest.supplierId().toString()));
        stockOrder.setSupplier(supplier);

        Set<DeliveryNoteDto> deliveryNotes = stockOrderDtoRequest.deliveryNotes();
        deliveryNotes.forEach(dn -> {
            ProductStockWrapper wrapper = getValidatedProductAndStock(dn.productId());
            Product product = wrapper.product();
            Stock stock = wrapper.stock();

            stock.setQuantity(dn.quantity());
            stockRepository.save(stock);

            DeliveryNote deliveryNote = new DeliveryNote();
            deliveryNote.setProduct(product);
            deliveryNote.setQuantity(dn.quantity());
            deliveryNote.setOrder(stockOrder);
            deliveryNoteRepository.save(deliveryNote);
        });

        StockOrder order = stockOrderRepository.save(stockOrder);
        return stockOrderMapper.toDto(order);
    }

    @Override
    public void deleteStockOrder(Long code) {
        StockOrder stockOrder = stockOrderRepository.findById(code)
                .orElseThrow(() -> new ResourceNotFoundException("Stock Order", "code", code.toString()));
        stockOrderRepository.delete(stockOrder);
    }

    /**
     * Validates product existence and ensures stock is available.
     */
    private ProductStockWrapper getValidatedProductAndStock(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId.toString()));

        Stock stock = product.getStock();
        if (stock == null) {
            throw new AppException(HttpStatus.BAD_REQUEST,
                    String.format("Product with id %d doesn't have stock.", product.getId()));
        }

        return new ProductStockWrapper(product, stock);
    }

    /**
         * Simple wrapper to return both Product and Stock together.
         */
        private record ProductStockWrapper(Product product, Stock stock) {

    }
}