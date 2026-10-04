package com.business.business_management_api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor // Thêm dòng này: Tạo constructor rỗng (để MapStruct dùng lệnh new)
@AllArgsConstructor // Thêm dòng này: Tạo constructor full tham số ở dạng public
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String imageUrl;
//    @JsonProperty("isActive")
    private boolean isActive;
}
