package com.example.bilabonnement.exceptions;

public class CarLeasingException extends RuntimeException {

    private final carExceptionEnum page;

    public CarLeasingException(String message, carExceptionEnum page) {
        super(message);
        this.page = page;
    }

    public carExceptionEnum getPage() {
        return page;
    }
}
