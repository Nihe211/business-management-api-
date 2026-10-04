package com.business.business_management_api.repository;

import com.business.business_management_api.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {
    // Kiểm tra tên trùng nhưng phải bỏ qua những sản phẩm đã xóa
    boolean existsByNameAndIsActiveTrue(String name);

    // Chỉ tìm chi tiết sản phẩm khi nó chưa bị xóa
    Optional<Product> findByIdAndIsActiveTrue(Long id);

    // Lấy danh sách sản phẩm chưa bị xóa (cho trang chủ)
    Page<Product> findAllByIsActiveTrue(Pageable pageable);
}
