package com.example.bilabonnement.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

//Frederik

/*This class is used to handle exceptions thrown by the controller in our spring application
* The handleCarLeasingException method is annotated with @ExceptionHandler, indicating that it will be called to handle
* any CarLeasingExceptions thrown in the application.
* The method creates a CarException object containing details about the exception, including the exception message and the HTTP status code,
* and returns it as a response entity with the appropriate HTTP status code. This allows the application to provide a customized response to any CarLeasingExceptions that are thrown.
* */
@ControllerAdvice
public class CarLeasingExceptionHandler {

    @ExceptionHandler(value = CarLeasingException.class)
    public ResponseEntity<Object> handleCarLeasingException(CarLeasingException e){
        // 1. create payload containing exception details


        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        CarException carException = new CarException(
                e.getMessage(), badRequest, ZonedDateTime.now(ZoneId.of("Z"))
        );
        // 2. Return response entity
        return new ResponseEntity<>(carException, badRequest);
    }
}
