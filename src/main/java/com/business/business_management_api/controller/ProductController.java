package com.business.business_management_api.controller;

import com.business.business_management_api.dto.request.ProductRequest;
import com.business.business_management_api.dto.response.ApiResponse;
import com.business.business_management_api.dto.response.ProductResponse;
import com.business.business_management_api.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    // 1. Lấy danh sách sản phẩm (Có phân trang: ?page=0&size=10)
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProducts(Pageable pageable) {
        Page<ProductResponse> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    // 2. Lấy chi tiết 1 sản phẩm
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    // 3. Thêm mới sản phẩm
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse newProduct = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(newProduct, "Tạo sản phẩm thành công"));
    }

    // 4. Cập nhật sản phẩm
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        ProductResponse updatedProduct = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.success(updatedProduct, "Cập nhật sản phẩm thành công"));
    }

    // 5. Xóa mềm sản phẩm
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
