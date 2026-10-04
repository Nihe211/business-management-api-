package com.business.business_management_api.exception;

public class ConflictException extends RuntimeException{
    public ConflictException(String mess){
        super(mess);
    }
}
