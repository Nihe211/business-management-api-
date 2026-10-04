package com.business.business_management_api.repository;

import com.business.business_management_api.entity.Order;
import com.business.business_management_api.enums.OrderStatus;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order,Long>, JpaSpecificationExecutor<Order> {
    Page<Order> findByUserId(Long userId, Pageable pageable);

    Page<Order> findByUserIdAndStatus(Long userId, OrderStatus status, Pageable pageable);

    Optional<Order> findByIdAndUserId(Long id, Long userId);

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
}
