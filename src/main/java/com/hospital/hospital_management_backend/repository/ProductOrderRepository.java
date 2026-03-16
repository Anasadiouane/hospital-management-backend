package com.hospital.hospital_management_backend.repository;

import com.hospital.hospital_management_backend.entity.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
    @Query("select po from ProductOrder po where po.id in (" +
            "select d.order.id from DeliveryNote d where d.product.id = :product_id)")
    List<ProductOrder> findAllByProduct(@Param("product_id") Long productId);
}