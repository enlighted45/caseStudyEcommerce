package com.caseStudy.E_commerce.ExceptionHandler;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message){
        super(message);
    }
}
