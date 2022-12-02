package com.example.bilabonnement.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

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
