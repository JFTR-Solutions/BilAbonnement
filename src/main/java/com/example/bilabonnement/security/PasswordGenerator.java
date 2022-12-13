package com.example.bilabonnement.security;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class PasswordGenerator {

    private final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZÆÅØabcdefghijklmnopqrstuvwxyzæåø0123456789!#%&/()?";

    //Thomas
    public String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(CHARS.length());
            password.append(CHARS.charAt(randomIndex));
        }
        return password.toString();
    }


}
