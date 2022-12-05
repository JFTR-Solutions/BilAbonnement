package com.example.bilabonnement.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncrypterTest {

    //Thomas

    //arrange
    Encrypter encrypter = new Encrypter();

    @Test
    void encrypt() {
        //act
        String encryptedPassword = encrypter.encrypt("CONXpIEsXx");

        //assert
        assertEquals("@@DgfM4XK{K[6oXW", encryptedPassword);
    }

    @Test
    void decrypt() {
        //act
        String decryptedPassword = encrypter.decrypt("@@DgfM4XK{K[6oXW");

        //assert
        assertEquals("CONXpIEsXx", decryptedPassword);
    }
}