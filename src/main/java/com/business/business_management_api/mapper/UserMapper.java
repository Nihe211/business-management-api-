package com.business.business_management_api.mapper;

import com.business.business_management_api.dto.request.RegisterRequest;
import com.business.business_management_api.dto.response.UserResponse;
import com.business.business_management_api.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserResponse toUserResponse(User user);
    User toUser(RegisterRequest request);
}
