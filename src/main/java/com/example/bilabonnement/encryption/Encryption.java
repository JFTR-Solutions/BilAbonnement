package com.example.bilabonnement.encryption;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Encryption {

    private static final int OFFSET = 3;

    public static String encrypt(String password) {
        String b64encoded = Base64.getEncoder().encode(password.getBytes(StandardCharsets.UTF_8)).toString();

        String reverse = new StringBuffer(b64encoded).reverse().toString();

        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < reverse.length(); i++) {
            temp.append((char) (reverse.charAt(i) + OFFSET));
        }
        return temp.toString();
    }

    public static String decrypt(String encryptedPassword) {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < encryptedPassword.length(); i++) {
            temp.append((char) (encryptedPassword.charAt(i) - OFFSET));
        }
        String reversed = new StringBuffer(temp.toString()).reverse().toString();
        return new String(Base64.getDecoder().decode(reversed));
    }
}