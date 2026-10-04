package com.business.business_management_api.service.impl;

import com.business.business_management_api.dto.request.OrderRequest;
import com.business.business_management_api.dto.response.OrderResponse;
import com.business.business_management_api.entity.Order;
import com.business.business_management_api.entity.OrderItem;
import com.business.business_management_api.entity.Product;
import com.business.business_management_api.entity.User;
import com.business.business_management_api.enums.OrderStatus;
import com.business.business_management_api.exception.BadRequestException;
import com.business.business_management_api.exception.ResourceNotFoundException;
import com.business.business_management_api.mapper.OrderMapper;
import com.business.business_management_api.repository.OrderRepo;
import com.business.business_management_api.repository.ProductRepo;
import com.business.business_management_api.repository.UserRepo;
import com.business.business_management_api.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepository;
    private final ProductRepo productRepository;
    private final UserRepo userRepository;
    private final OrderMapper orderMapper;
    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin người dùng"));
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(request.getShippingAddress());
        order.setNote(request.getNote());
        order.setStatus(OrderStatus.PENDING);

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderRequest.OrderItemRequest itemReq : request.getItems()){
            Product product = productRepository.findByIdAndIsActiveTrue(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm ID " + itemReq.getProductId() + " không tồn tại hoặc đã ngừng kinh doanh"));
            if(product.getStockQuantity()<itemReq.getQuantity()){
                throw new BadRequestException("Sản phẩm " + product.getName() + " không đủ số lượng (Còn: " + product.getStockQuantity() + ")");
            }
            product.setStockQuantity(product.getStockQuantity()-itemReq.getQuantity());
            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            item.setUnitPrice(product.getPrice());

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
            order.addItem(item);
        }
        order.setTotalAmount(totalAmount);
        Order saved = orderRepository.save(order);
        return orderMapper.toOrderResponse(saved);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));

        // Xác thực quyền sở hữu đơn hàng
        if (!order.getUser().getId().equals(userId)) {
            throw new BadRequestException("Bạn không có quyền thao tác trên đơn hàng này");
        }

        // Khách hàng chỉ được hủy khi đơn đang chờ xử lý
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BadRequestException("Chỉ có thể hủy đơn hàng ở trạng thái đang chờ xử lý (PENDING)");
        }

        order.setStatus(OrderStatus.CANCELLED);

        // Hoàn lại tồn kho cho từng sản phẩm trong đơn
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
        }

        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toOrderResponse(updatedOrder);
    }

    @Override
    public Page<OrderResponse> getMyOrders(Long userId, OrderStatus status, Pageable pageable) {
        Page<Order> orders;
        if (status != null) {
            orders = orderRepository.findByUserIdAndStatus(userId, status, pageable);
        } else {
            orders = orderRepository.findByUserId(userId, pageable);
        }
        return orders.map(orderMapper::toOrderResponse);
    }

    @Override
    public OrderResponse getOrderById(Long orderId, Long userId, String role) {
        Order order;
        if ("USER".equals(role)) {
            // Nếu là USER: Bắt buộc DB phải tìm đơn hàng khớp cả ID và UserId
            order = orderRepository.findByIdAndUserId(orderId, userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));
        } else {
            // Nếu là ADMIN: Tìm tự do theo ID
            order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));
        }

        return orderMapper.toOrderResponse(order);
    }

    @Override
    public Page<OrderResponse> getAllOrders(OrderStatus status, Pageable pageable) {
        Page<Order> orders;
        if (status != null) {
            orders = orderRepository.findByStatus(status, pageable);
        } else {
            orders = orderRepository.findAll(pageable);
        }
        return orders.map(orderMapper::toOrderResponse);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()->new ResourceNotFoundException("Không tìm thấy đơn hàng"));
        OrderStatus currentStatus = order.getStatus();
        // 1. Chặn thay đổi nếu đơn đã đóng (Hủy hoặc Hoàn thành)
        if (currentStatus == OrderStatus.CANCELLED || currentStatus == OrderStatus.COMPLETED) {
            throw new BadRequestException("Không thể thay đổi trạng thái của đơn hàng đã Hủy hoặc Hoàn thành");
        }

        // 2. Kiểm tra luồng trạng thái hợp lệ (State Machine)
        if (newStatus == OrderStatus.SHIPPING && currentStatus != OrderStatus.CONFIRMED) {
            throw new BadRequestException("Đơn hàng phải được XÁC NHẬN (CONFIRMED) trước khi GIAO HÀNG (SHIPPING)");
        }
        if (newStatus == OrderStatus.COMPLETED && currentStatus != OrderStatus.SHIPPING) {
            throw new BadRequestException("Đơn hàng phải ĐANG GIAO (SHIPPING) mới có thể HOÀN THÀNH (COMPLETED)");
        }

        // 3. Nếu Admin hủy đơn -> Phải hoàn lại tồn kho
        if (newStatus == OrderStatus.CANCELLED) {
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            }
        }

        // 4. Cập nhật và lưu
        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toOrderResponse(updatedOrder);



    }
}
