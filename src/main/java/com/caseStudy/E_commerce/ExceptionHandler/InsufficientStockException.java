package com.caseStudy.E_commerce.ExceptionHandler;

public class InsufficientStockException extends RuntimeException{
    public InsufficientStockException(String message){
        super(message);
    }
}
