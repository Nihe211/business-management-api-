package com.business.business_management_api.mapper;

import com.business.business_management_api.dto.request.ProductRequest;
import com.business.business_management_api.dto.response.ProductResponse;
import com.business.business_management_api.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @org.mapstruct.Builder(disableBuilder = true))
public interface ProductMapper {
    ProductResponse toProductResponse(Product product);
    @Mapping(target = "active", constant = "true")
    Product toProduct(ProductRequest request);
    void updateProductFromRequest(ProductRequest request, @MappingTarget Product product);
}
