package com.example.bilabonnement.security;

import com.example.bilabonnement.service.UserService;

import java.sql.Date;

public class UsernameMaker {

    static UserService userService;

    public UsernameMaker(UserService userService) {
        this.userService = userService;
    }

    public static String makeUsername(String firstName, String lastName, Date dateOfBirth) {
        String username = firstName.substring(0, 1).toLowerCase() + "_" + lastName.toLowerCase() + dateOfBirth.toLocalDate().getYear();

        if(userService.checkIfUsernameExists(username)) {
            username = username + "1";
        }
        return username;
    }

}