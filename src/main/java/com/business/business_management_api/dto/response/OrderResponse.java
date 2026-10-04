package com.business.business_management_api.dto.response;

import com.business.business_management_api.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private String customerName; // Lấy từ User
    private BigDecimal totalAmount;
    private OrderStatus status;
    private String shippingAddress;
    private String note;
    private LocalDateTime createdAt;

    private List<OrderItemResponse> items;

    @Data
    @Builder
    public static class OrderItemResponse {
        private Long productId;
        private String productName;
        private Integer quantity;
        private BigDecimal unitPrice;
    }
}
