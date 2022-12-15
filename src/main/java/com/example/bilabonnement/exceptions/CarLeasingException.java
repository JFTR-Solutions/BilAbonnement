package com.example.bilabonnement.exceptions;

import java.util.HashMap;

//FREDERIK

/*This code defines a custom CarLeasingException class that extends the RuntimeException class.
A RuntimeException is a type of exception that occurs during the execution of a program, and is not checked by the compiler.

The CarLeasingException class has a constructor that takes a message string as an argument.
This message is passed to the super class (RuntimeException) and will be used to provide information about the exception when it is thrown.
The CarLeasingException class can be thrown in a program to indicate that an error has occurred related to car leasing. This allows the application to provide a custom response to the exception.
*/

public class CarLeasingException extends RuntimeException {

    public CarLeasingException(String message) {
        super(message);
    }

}
