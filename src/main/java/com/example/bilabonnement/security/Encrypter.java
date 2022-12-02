package com.example.bilabonnement.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Encrypter {

    //Thomas

    private final int OFFSET = 3;
    private final int OFFSET2 = 4;


    public String encrypt(String password) {

        StringBuilder temp1 = new StringBuilder();
        for (int i = 0; i < password.length(); i++) {
            temp1.append((char) (password.charAt(i) + OFFSET2));
        }

        int cut = password.length()/2;

        String finalStep = temp1.substring(cut) + temp1.substring(0,cut);

        String b64encoded = Base64.getEncoder().encodeToString(finalStep.getBytes(StandardCharsets.UTF_8));

        String reverse = new StringBuffer(b64encoded).reverse().toString();

        StringBuilder temp2 = new StringBuilder();
        for (int i = 0; i < reverse.length(); i++) {
            temp2.append((char) (reverse.charAt(i) + OFFSET));
        }

        return temp2.toString();
    }


    public String decrypt(String encryptedPassword) {

        StringBuilder temp1 = new StringBuilder();
        for (int i = 0; i < encryptedPassword.length(); i++) {
            temp1.append((char) (encryptedPassword.charAt(i) - OFFSET));
        }
        String reversed = new StringBuffer(temp1.toString()).reverse().toString();

        String firstStep = new String(Base64.getDecoder().decode(reversed));

        int cut = firstStep.length()/2;
        String finalStep;
        if(firstStep.length() % 2 != 0) {
            finalStep = firstStep.substring(cut + 1) + firstStep.substring(0, cut + 1);
        } else finalStep = firstStep.substring(cut) + firstStep.substring(0, cut);

        StringBuilder temp2 = new StringBuilder();
        for (int i = 0; i < finalStep.length(); i++) {
            temp2.append((char) (finalStep.charAt(i) - OFFSET2));
        }
        return temp2.toString();
    }


    public static void main(String[] args) {
        String b64encoded1 = Base64.getEncoder().encodeToString("ABCDEFGHIJKLMNOPQRSTUVWXYZÆØÅ".getBytes(StandardCharsets.UTF_8));
        String b64encoded2 = Base64.getEncoder().encodeToString("abcdefghijklmnopqrstuvwxyzæøå".getBytes(StandardCharsets.UTF_8));
        String b64encoded3 = Base64.getEncoder().encodeToString("0123456789".getBytes(StandardCharsets.UTF_8));
        String b64encoded4 = Base64.getEncoder().encodeToString("!#¤%&/()=?`^*><½§".getBytes(StandardCharsets.UTF_8));


        System.out.println(b64encoded1);
        System.out.println(b64encoded2);
        System.out.println(b64encoded3);
        System.out.println(b64encoded4);
    }
}

