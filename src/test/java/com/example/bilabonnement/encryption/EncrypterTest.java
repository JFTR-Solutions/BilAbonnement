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
        String encryptedPassword = encrypter.encrypt("ÆÅØ!/#¤%&/()=?`´^¨*+~{[]}|\\:;<>@£$€¥¢§½¬");

        //assert
        assertEquals("y7FuFOJxFW5TE4FO}rVNrN;M}XFqGq7zNRPvFK7zuNvsFqtzzNr7rftzHMHT27G\\DNfjFK5[2Mrz", encryptedPassword);
    }

    @Test
    void decrypt() {
        //act
        String decryptedPassword = encrypter.decrypt("y7FuFOJxFW5TE4FO}rVNrN;M}XFqGq7zNRPvFK7zuNvsFqtzzNr7rftzHMHT27G\\DNfjFK5[2Mrz");

        //assert
        assertEquals("ÆÅØ!/#¤%&/()=?`´^¨*+~{[]}|\\:;<>@£$€¥¢§½¬", decryptedPassword);
    }
}