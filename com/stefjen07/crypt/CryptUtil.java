package com.stefjen07.crypt;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class CryptUtil {
    KeyGenerator keyGenerator;
    Cipher cipher;

    public CryptUtil(CryptPreset preset) throws NoSuchAlgorithmException, NoSuchPaddingException {
        switch(preset) {
            case aes:
                keyGenerator = KeyGenerator.getInstance("AES");
                keyGenerator.init(128);
                //System.out.println(new BigInteger(keyGenerator.generateKey().getEncoded()).toString(16));

                cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                break;
        }
    }

    public SecretKey generateKey() {
        return keyGenerator.generateKey();
    }

    public IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public String encrypt(String input, SecretKey key,
                                 IvParameterSpec iv) throws InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    public String decrypt(String cipherText, SecretKey key,
                                 IvParameterSpec iv) throws InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
                .decode(cipherText));
        return new String(plainText);
    }
}
