package com.business.business_management_api.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String mess){
        super(mess);
    }
}
