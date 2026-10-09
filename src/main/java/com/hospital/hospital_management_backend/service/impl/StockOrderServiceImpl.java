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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service implementation for managing stock orders.
 * A stock order ADDS quantity to the stock of each product.
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

    // =========================
    // CREATE
    // =========================
    @Override
    @Transactional
    public StockOrderDtoResponse createStockOrder(StockOrderDtoRequest request) {
        Supplier supplier = findSupplier(request.supplierId());

        StockOrder stockOrder = stockOrderMapper.toEntity(request);
        stockOrder.setSupplier(supplier);

        StockOrder saved = stockOrderRepository.save(stockOrder);
        addDeliveryNotes(request.deliveryNotes(), saved);

        return stockOrderMapper.toDto(saved);
    }

    // =========================
    // READ
    // =========================
    @Override
    @Transactional(readOnly = true)
    public StockOrderDtoResponse getStockOrder(Long code) {
        return stockOrderMapper.toDto(findStockOrder(code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockOrderDtoResponse> getAllStockOrder() {
        return stockOrderRepository.findAll().stream()
                .map(stockOrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockOrderDtoResponse> getStockOrdersOfProduct(Long productId) {
        findProduct(productId);

        // filter at DTO level: never modify managed entities while reading
        return stockOrderRepository.findAllByProduct(productId).stream()
                .map(stockOrderMapper::toDto)
                .map(dto -> {
                    Set<StockOrderDtoResponse.DeliveryNoteDto> filtered = dto.deliveryNotes().stream()
                            .filter(dn -> dn.product().id().equals(productId))
                            .collect(Collectors.toSet());
                    return new StockOrderDtoResponse(
                            dto.code(),
                            dto.orderDate(),
                            filtered,
                            dto.supplier(),
                            dto.deliveryLocation()
                    );
                })
                .collect(Collectors.toList());
    }

    // =========================
    // UPDATE
    // =========================
    @Override
    @Transactional
    public StockOrderDtoResponse updateStockOrder(Long code, StockOrderDtoRequest request) {
        StockOrder stockOrder = findStockOrder(code);

        if (StringUtils.hasText(request.deliveryLocation())) {
            stockOrder.setDeliveryLocation(request.deliveryLocation());
        }
        stockOrder.setSupplier(findSupplier(request.supplierId()));

        // 1) undo the effect of the old delivery notes on the stock
        reverseStock(stockOrder.getDeliveryNotes());

        // 2) delete the old delivery notes
        if (stockOrder.getDeliveryNotes() != null) {
            deliveryNoteRepository.deleteAll(new ArrayList<>(stockOrder.getDeliveryNotes()));
            stockOrder.getDeliveryNotes().clear();
        }

        // 3) apply the new delivery notes
        addDeliveryNotes(request.deliveryNotes(), stockOrder);

        return stockOrderMapper.toDto(stockOrderRepository.save(stockOrder));
    }

    // =========================
    // DELETE
    // =========================
    @Override
    @Transactional
    public void deleteStockOrder(Long code) {
        StockOrder stockOrder = findStockOrder(code);
        reverseStock(stockOrder.getDeliveryNotes());
        stockOrderRepository.delete(stockOrder);
    }

    // =========================
    // HELPERS
    // =========================
    private void addDeliveryNotes(Set<DeliveryNoteDto> notes, StockOrder order) {
        if (order.getDeliveryNotes() == null) {
            order.setDeliveryNotes(new HashSet<>());
        }

        for (DeliveryNoteDto dn : notes) {
            Product product = findProduct(dn.productId());
            Stock stock = requireStock(product);

            stock.setQuantity(stock.getQuantity() + dn.quantity());
            stockRepository.save(stock);

            DeliveryNote note = new DeliveryNote();
            note.setProduct(product);
            note.setQuantity(dn.quantity());
            note.setOrder(order);
            order.getDeliveryNotes().add(deliveryNoteRepository.save(note));
        }
    }

    /** Removes from the stock what this order had added. */
    private void reverseStock(Set<DeliveryNote> notes) {
        if (notes == null || notes.isEmpty()) return;

        for (DeliveryNote dn : notes) {
            Stock stock = dn.getProduct().getStock();
            if (stock == null) continue;

            long newQuantity = stock.getQuantity() - dn.getQuantity();
            if (newQuantity < 0) {
                throw new AppException(HttpStatus.BAD_REQUEST,
                        String.format("Cannot change this order: the stock of product %d was already used",
                                dn.getProduct().getId()));
            }
            stock.setQuantity(newQuantity);
            stockRepository.save(stock);
        }
    }

    private StockOrder findStockOrder(Long code) {
        return stockOrderRepository.findById(code)
                .orElseThrow(() -> new ResourceNotFoundException("Stock order", "code", code.toString()));
    }

    private Supplier findSupplier(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id.toString()));
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id.toString()));
    }

    private Stock requireStock(Product product) {
        Stock stock = product.getStock();
        if (stock == null) {
            throw new AppException(HttpStatus.BAD_REQUEST,
                    String.format("Product with id %d doesn't have stock.", product.getId()));
        }
        return stock;
    }
}
