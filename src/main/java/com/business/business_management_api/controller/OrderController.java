package com.business.business_management_api.controller;

import com.business.business_management_api.dto.request.OrderRequest;
import com.business.business_management_api.dto.response.ApiResponse;
import com.business.business_management_api.dto.response.OrderResponse;
import com.business.business_management_api.enums.OrderStatus;
import com.business.business_management_api.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // 1. Khách hàng đặt đơn mới
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @Valid @RequestBody OrderRequest request,
            @RequestHeader("X-User-Id") Long userId) { // TODO: Sẽ thay bằng JWT Token sau
        OrderResponse newOrder = orderService.createOrder(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(newOrder, "Đặt hàng thành công"));
    }

    // 2. Khách hàng xem lịch sử đơn hàng của chính mình
    @GetMapping("/my-orders")
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getMyOrders(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false) OrderStatus status,
            @ParameterObject Pageable pageable) {
        Page<OrderResponse> orders = orderService.getMyOrders(userId, status, pageable);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    // 3. Khách hàng hủy đơn (Chỉ khi PENDING)
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        OrderResponse canceledOrder = orderService.cancelOrder(id, userId);
        return ResponseEntity.ok(ApiResponse.success(canceledOrder, "Hủy đơn hàng thành công"));
    }


    // --- API DÀNH CHUNG HOẶC ADMIN ---

    // 4. Xem chi tiết 1 đơn hàng
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader(value = "X-Role", defaultValue = "USER") String role) { // Giả lập Role
        OrderResponse order = orderService.getOrderById(id, userId, role);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    // 5. Admin xem toàn bộ đơn hàng của hệ thống
    @GetMapping
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getAllOrders(
            @RequestParam(required = false) OrderStatus status,
            Pageable pageable) {
        Page<OrderResponse> orders = orderService.getAllOrders(status, pageable);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    // 6. Admin cập nhật trạng thái đơn hàng (CONFIRMED, SHIPPING, COMPLETED...)
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        OrderResponse updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(updatedOrder, "Cập nhật trạng thái thành công"));
    }
}
