package com.business.business_management_api.service;

import com.business.business_management_api.dto.request.ProductRequest;
import com.business.business_management_api.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponse createProduct (ProductRequest request);
    ProductResponse updateProduct(Long id, ProductRequest request);
    ProductResponse getProductById(Long id);
    Page<ProductResponse> getAllProducts(Pageable pageable);
    void deleteProduct(Long id);
}
