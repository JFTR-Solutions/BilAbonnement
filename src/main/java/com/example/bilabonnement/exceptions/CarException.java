package com.example.bilabonnement.exceptions;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

//FREDERIK

/*
This class is used to provide information about an exception that has occured.
The CarException class has three fields: message, httpStatus, and timestamp.
The message field contains a description of the error that occurred.
The httpStatus field contains the HTTP status code that should be used in the response to the exception.
The timestamp field contains the time at which the exception occurred.*/
public class CarException {

    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;

    public CarException(String message, HttpStatus httpStatus, ZonedDateTime timestamp) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
}
