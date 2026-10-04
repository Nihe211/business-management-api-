package com.business.business_management_api.entity;

import com.business.business_management_api.enums.OrderStatus;
import com.business.business_management_api.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    private String note;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    // Hàm tiện ích để thêm Item vào Order đồng thời set quan hệ 2 chiều
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}
