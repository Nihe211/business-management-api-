package com.business.business_management_api.service;

import com.business.business_management_api.dto.request.OrderRequest;
import com.business.business_management_api.dto.response.OrderResponse;
import com.business.business_management_api.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    // Dành cho Customer (Khách hàng)
    OrderResponse createOrder(OrderRequest request, Long userId);
    OrderResponse cancelOrder(Long orderId, Long userId);
    Page<OrderResponse> getMyOrders(Long userId, OrderStatus status, Pageable pageable);

    // Dành chung cho cả Customer và Admin
    OrderResponse getOrderById(Long orderId, Long userId, String role);

    // Dành cho Admin
    Page<OrderResponse> getAllOrders(OrderStatus status, Pageable pageable);
    OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus);
}
