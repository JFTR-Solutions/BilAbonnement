package com.example.bilabonnement.security;

import com.example.bilabonnement.service.UserService;

import java.sql.Date;
import java.util.Random;

public class UsernameMaker {

    UserService userService;

    public UsernameMaker(UserService userService) {
        this.userService = userService;
    }

    public String makeUsername(String firstName, String lastName, Date dateOfBirth) {
        String username = firstName.substring(0, 1).toLowerCase() + "_" + lastName.toLowerCase() +
                dateOfBirth.toLocalDate().getYear();

        if(userService.checkIfUsernameExists(username)) {
            Random rd = new Random();
            username = username + (rd.nextInt(100) + 1);
        }
        return username;
    }
}
