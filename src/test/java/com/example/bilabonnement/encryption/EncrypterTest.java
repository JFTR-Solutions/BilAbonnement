package com.example.bilabonnement.encryption;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncrypterTest {

    //Thomas

    //arrange
    Encrypter encrypter = new Encrypter();

    @Test
    void encrypt() {
        //act
        String encryptedPassword = encrypter.encrypt("1234");

        //assert
        assertEquals("@@jQ4j}Q", encryptedPassword);
    }

    @Test
    void decrypt() {
        //act
        String decryptedPassword = encrypter.decrypt("@@jQ4j}Q");

        //assert
        assertEquals("1234", decryptedPassword);
    }
}