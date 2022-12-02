package com.example.bilabonnement.exceptions;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class CarException {

    private final String message;
    private final carExceptionEnum page;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;

    public CarException(String message,
                        carExceptionEnum page, HttpStatus httpStatus, ZonedDateTime timestamp) {
        this.message = message;
        this.page = page;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public carExceptionEnum getPage() {
        return page;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
}
