package com.business.business_management_api.dto.response;

import com.business.business_management_api.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private Role role;
    private boolean isActive;
}
