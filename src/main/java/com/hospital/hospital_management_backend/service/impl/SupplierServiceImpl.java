package com.hospital.hospital_management_backend.service.impl;

import com.hospital.hospital_management_backend.dto.request.SupplierDtoRequest;
import com.hospital.hospital_management_backend.dto.response.StockOrderDtoResponse;
import com.hospital.hospital_management_backend.dto.response.SupplierDtoResponse;
import com.hospital.hospital_management_backend.entity.Supplier;
import com.hospital.hospital_management_backend.exception.AppException;
import com.hospital.hospital_management_backend.exception.ResourceNotFoundException;
import com.hospital.hospital_management_backend.mapper.StockOrderMapper;
import com.hospital.hospital_management_backend.mapper.SupplierMapper;
import com.hospital.hospital_management_backend.repository.SupplierRepository;
import com.hospital.hospital_management_backend.service.SupplierService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    private final StockOrderMapper stockOrderMapper;

    public SupplierServiceImpl(SupplierRepository supplierRepository,
                               SupplierMapper supplierMapper,
                               StockOrderMapper stockOrderMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
        this.stockOrderMapper = stockOrderMapper;
    }

    @Override
    @Transactional
    public SupplierDtoResponse createSupplier(SupplierDtoRequest dto) {
        if (supplierRepository.existsByPhone(dto.phone())) {
            throw new AppException(HttpStatus.CONFLICT, "Phone number already exists");
        }
        if (supplierRepository.existsByEmail(dto.email())) {
            throw new AppException(HttpStatus.CONFLICT, "Email already exists");
        }
        return supplierMapper.toDto(supplierRepository.save(supplierMapper.toEntity(dto)));
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierDtoResponse getById(Long supplierId) {
        return supplierMapper.toDto(findSupplier(supplierId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierDtoResponse> getAllSupplier() {
        return supplierMapper.toDtoList(supplierRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockOrderDtoResponse> getStockOrdersForSupplier(Long supplierId) {
        Supplier supplier = findSupplier(supplierId);
        return stockOrderMapper.toDtoList(new ArrayList<>(supplier.getStockOrders()));
    }

    @Override
    @Transactional
    public SupplierDtoResponse updateSupplier(Long supplierId, SupplierDtoRequest dto) {
        Supplier supplier = findSupplier(supplierId);

        // only check uniqueness when the value really changes
        if (!dto.phone().equals(supplier.getPhone()) && supplierRepository.existsByPhone(dto.phone())) {
            throw new AppException(HttpStatus.CONFLICT, "Phone number already exists");
        }
        if (!dto.email().equals(supplier.getEmail()) && supplierRepository.existsByEmail(dto.email())) {
            throw new AppException(HttpStatus.CONFLICT, "Email already exists");
        }

        supplierMapper.updateFromDto(dto, supplier);

        return supplierMapper.toDto(supplierRepository.save(supplier));
    }

    @Override
    @Transactional
    public void deleteSupplier(Long supplierId) {
        supplierRepository.delete(findSupplier(supplierId));
    }

    private Supplier findSupplier(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id.toString()));
    }
}
