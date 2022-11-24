package com.example.bilabonnement.encryption;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionTest {

    //arrange
    Encryption encryption = new Encryption();

    @Test
    void encrypt() {
        //act
        String encryptedPassword = encryption.encrypt("1234");

        //assert
        assertEquals("@@DQ}LWP", encryptedPassword);
    }

    @Test
    void decrypt() {
        //act
        String decryptedPassword = encryption.decrypt("@\\JdXYGQ}LWP");

        //assert
        assertEquals("12345Thf", decryptedPassword);
    }
}