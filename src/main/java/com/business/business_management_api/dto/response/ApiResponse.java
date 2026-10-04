package com.business.business_management_api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Quan trọng: Nếu trường nào null, Jackson sẽ tự động ẩn nó đi khi parse ra JSON
public class ApiResponse<T> {

    private int code;
    private String message;
    private T data;

    // 1. Dành cho GET, PUT (200 OK)
    public static <T> ApiResponse<T> success(T data,String message) {
        return ApiResponse.<T>builder()
                .code(HttpStatus.OK.value()) // 200
                .message(message)
                .data(data)
                .build();
    }

    // 2. Dành cho POST tạo mới (201 Created)
    public static <T> ApiResponse<T> created(T data, String message) {
        return ApiResponse.<T>builder()
                .code(HttpStatus.CREATED.value()) // 201
                .message(message)
                .data(data)
                .build();
    }

    // 3. Dành cho các trường hợp custom code khác
    public static <T> ApiResponse<T> success(HttpStatus status, String message, T data) {
        return ApiResponse.<T>builder()
                .code(status.value())
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(HttpStatus.OK.value()) // 200
                .message("Thành Công")
                .data(data)
                .build();
    }
}
