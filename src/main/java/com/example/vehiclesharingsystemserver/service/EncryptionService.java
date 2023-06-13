package com.example.vehiclesharingsystemserver.service;

import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Service
public class EncryptionService {

    private static final String encodedKey = "V2n/zFD7TNiH/TZaHhcI8lnsW3uN3/+aNLkoXg0246A=";
    private static final String algorithm = "AES/CBC/PKCS5Padding";
    private static final String encodedIv = "vuFJ5vKBYi3hUAPD5t3mFg==";


    private static SecretKey decodeKey()
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return  new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    private static IvParameterSpec getIv(){
        byte[] decodedIV = Base64.getDecoder().decode(encodedIv);
        return new IvParameterSpec(decodedIV);
    }


    public static String encrypt(String input) throws
            BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, InvalidKeySpecException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, decodeKey(),getIv());
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }
}
