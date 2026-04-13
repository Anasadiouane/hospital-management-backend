package com.hospital.hospital_management_backend.service.impl;

import com.hospital.hospital_management_backend.dto.request.DeliveryNoteDto;
import com.hospital.hospital_management_backend.dto.request.ProductOrderDtoRequest;
import com.hospital.hospital_management_backend.dto.response.ProductOrderDtoResponse;
import com.hospital.hospital_management_backend.entity.*;
import com.hospital.hospital_management_backend.exception.AppException;
import com.hospital.hospital_management_backend.exception.ResourceNotFoundException;
import com.hospital.hospital_management_backend.mapper.ProductOrderMapper;
import com.hospital.hospital_management_backend.repository.*;
import com.hospital.hospital_management_backend.service.ProductOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service implementation for managing product orders.
 */
@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    private final ProductOrderRepository productOrderRepository;
    private final ServiceRepository serviceRepository;
    private final DeliveryNoteRepository deliveryNoteRepository;
    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final ProductOrderMapper productOrderMapper;

    public ProductOrderServiceImpl(ProductOrderRepository productOrderRepository,
                                   ServiceRepository serviceRepository,
                                   DeliveryNoteRepository deliveryNoteRepository,
                                   StockRepository stockRepository,
                                   ProductRepository productRepository,
                                   ProductOrderMapper productOrderMapper) {
        this.productOrderRepository = productOrderRepository;
        this.serviceRepository = serviceRepository;
        this.deliveryNoteRepository = deliveryNoteRepository;
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
        this.productOrderMapper = productOrderMapper;
    }

    // =========================
    // CREATE
    // =========================
    @Override
    @Transactional
    public ProductOrderDtoResponse createProductOrder(ProductOrderDtoRequest request) {
        HospitalService hospitalService = findServiceByName(request.hospitalServiceName());

        ProductOrder productOrder = productOrderMapper.toEntity(request);
        productOrder.setHospitalService(hospitalService);

        ProductOrder savedOrder = productOrderRepository.save(productOrder);

        processDeliveryNotes(request.deliveryNotes(), savedOrder);

        return productOrderMapper.toDto(findProductOrder(savedOrder.getId()));
    }

    // =========================
    // READ
    // =========================
    @Override
    @Transactional(readOnly = true)
    public ProductOrderDtoResponse getProductOrder(Long id) {
        return productOrderMapper.toDto(findProductOrder(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductOrderDtoResponse> getAllProductOrder() {
        return productOrderRepository.findAll()
                .stream()
                .map(productOrderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductOrderDtoResponse> getProductOrdersOfProduct(Long productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId.toString()));

        return productOrderRepository.findAllByProduct(productId)
                .stream()
                .map(productOrder -> {
                    ProductOrderDtoResponse dto = productOrderMapper.toDto(productOrder);
                    Set<ProductOrderDtoResponse.DeliveryNoteDto> filtered = dto.deliveryNotes()
                            .stream()
                            .filter(dn -> dn.product().id().equals(productId))
                            .collect(Collectors.toSet());
                    return new ProductOrderDtoResponse(
                            dto.code(),
                            dto.orderDate(),
                            filtered,
                            dto.hospitalServiceName()
                    );
                })
                .toList();
    }

    // =========================
    // UPDATE
    // =========================
    @Override
    @Transactional
    public ProductOrderDtoResponse updateProductOrder(Long id, ProductOrderDtoRequest request) {
        ProductOrder productOrder = findProductOrder(id);

        if (StringUtils.hasText(request.hospitalServiceName())) {
            productOrder.setHospitalService(findServiceByName(request.hospitalServiceName()));
        }

        // restore stock from old delivery notes
        restoreStock(productOrder.getDeliveryNotes());

        // delete old delivery notes
        deliveryNoteRepository.deleteAll(productOrder.getDeliveryNotes());
        productOrder.getDeliveryNotes().clear();

        // create new delivery notes and subtract stock
        processDeliveryNotes(request.deliveryNotes(), productOrder);

        productOrderMapper.updateFromDto(request, productOrder);

        return productOrderMapper.toDto(productOrderRepository.save(productOrder));
    }

    // =========================
    // DELETE
    // =========================
    @Override
    @Transactional
    public void deleteProductOrder(Long id) {
        ProductOrder productOrder = findProductOrder(id);
        restoreStock(productOrder.getDeliveryNotes());
        productOrderRepository.delete(productOrder);
    }

    // =========================
    // HELPERS
    // =========================
    private ProductOrder findProductOrder(Long id) {
        return productOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product order", "id", id.toString()));
    }

    private HospitalService findServiceByName(String name) {
        return serviceRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital service", "name", name));
    }

    private void processDeliveryNotes(Set<DeliveryNoteDto> deliveryNotes, ProductOrder order) {
        deliveryNotes.forEach(dn -> {
            ProductStockWrapper wrapper = getValidatedProductAndStock(dn.productId());
            Stock stock = wrapper.stock();

            long newQuantity = stock.getQuantity() - dn.quantity();
            if (newQuantity < 0) {
                throw new AppException(HttpStatus.BAD_REQUEST,
                        String.format("Not enough stock for product with id %d", wrapper.product().getId()));
            }

            stock.setQuantity(newQuantity);
            stockRepository.save(stock);

            deliveryNoteRepository.save(
                    DeliveryNote.builder()
                            .product(wrapper.product())
                            .quantity(dn.quantity())
                            .order(order)
                            .build()
            );
        });
    }

    private void restoreStock(Set<DeliveryNote> deliveryNotes) {
        if (deliveryNotes == null || deliveryNotes.isEmpty()) return;
        deliveryNotes.forEach(dn -> {
            Stock stock = dn.getProduct().getStock();
            if (stock != null) {
                stock.setQuantity(stock.getQuantity() + dn.getQuantity());
                stockRepository.save(stock);
            }
        });
    }

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

    // ✅ record instead of class
    private record ProductStockWrapper(Product product, Stock stock) {}
}