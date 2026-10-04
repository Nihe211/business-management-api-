package com.business.business_management_api.service.impl;

import com.business.business_management_api.dto.request.ProductRequest;
import com.business.business_management_api.dto.response.ProductResponse;
import com.business.business_management_api.entity.Product;
import com.business.business_management_api.exception.ConflictException;
import com.business.business_management_api.exception.ResourceNotFoundException;
import com.business.business_management_api.mapper.ProductMapper;
import com.business.business_management_api.repository.ProductRepo;
import com.business.business_management_api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepo repo;
    private final ProductMapper mapper;
    @Override
    public ProductResponse createProduct(ProductRequest request) {
        if(repo.existsByNameAndIsActiveTrue(request.getName())){
            throw new ConflictException("Tên sản phẩm đã tồn tại!");
        }
        Product product = mapper.toProduct(request);
        Product saved = repo.save(product);
        return mapper.toProductResponse(saved);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = repo.findByIdAndIsActiveTrue(id)
                .orElseThrow(()-> new ResourceNotFoundException("Không tìm thấy sản phẩm có ID: " + id));
        mapper.updateProductFromRequest(request,product);
        Product updated = repo.save(product);
        return mapper.toProductResponse(updated);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = repo.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm có ID: " + id));
        return mapper.toProductResponse(product);
    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> products = repo.findAllByIsActiveTrue(pageable);
        return products.map(mapper::toProductResponse);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product=repo.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm có ID: " + id));
        product.setActive(false);
        repo.save(product);

    }
}
