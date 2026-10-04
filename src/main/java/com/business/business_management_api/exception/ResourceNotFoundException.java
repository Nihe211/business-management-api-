package com.business.business_management_api.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String mess){
        super(mess);
    }
}
