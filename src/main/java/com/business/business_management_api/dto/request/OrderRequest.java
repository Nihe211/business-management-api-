package com.business.business_management_api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.*;

@Data
public class OrderRequest {
    @NotBlank(message = "Địa chỉ giao hàng không được để trống")
    private String shippingAddress;

    private String note;

    @NotEmpty(message = "Đơn hàng phải có ít nhất 1 sản phẩm")
    @Valid // Kích hoạt validate cho các phần tử bên trong List
    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        @NotNull(message = "ID sản phẩm không được để trống")
        private Long productId;

        @NotNull(message = "Số lượng không được để trống")
        @Min(value = 1, message = "Số lượng phải lớn hơn 0")
        private Integer quantity;
    }
}
