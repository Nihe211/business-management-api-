package com.business.business_management_api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp;

    private int status;
    private String error;
    private String message;
    private String path;
}
