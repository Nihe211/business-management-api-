package com.business.business_management_api.mapper;

import com.business.business_management_api.dto.response.OrderResponse;
import com.business.business_management_api.entity.Order;
import com.business.business_management_api.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    // 1. Map cho Order
    // MapStruct sẽ tự nhảy vào order.getUser().getFullName() để lấy giá trị gán cho customerName
    @Mapping(source = "user.fullName", target = "customerName")
    OrderResponse toOrderResponse(Order order);

    // 2. Map cho OrderItem (MapStruct sẽ tự động dùng hàm này khi biến đổi List<OrderItem> ở trên)
    // Tự động nhảy vào orderItem.getProduct() để lấy id và name
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    OrderResponse.OrderItemResponse toOrderItemResponse(OrderItem orderItem);
}
