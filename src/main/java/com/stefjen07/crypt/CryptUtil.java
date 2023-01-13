package com.stefjen07.crypt;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Component
public class CryptUtil {
    KeyGenerator keyGenerator;
    Cipher cipher;
    String algorithm;

    public CryptUtil() {
        this(CryptPreset.aes);
    }

    public CryptUtil(CryptPreset preset) {
        try {
            switch (preset) {
                case aes:
                    algorithm = "AES";
                    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SecretKey generateKey() throws NoSuchAlgorithmException {
        keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    }

    public SecretKey generateKey(int keySize, char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt, 1000, keySize);
        SecretKey pbeKey = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(pbeKeySpec);
        return new SecretKeySpec(pbeKey.getEncoded(), algorithm);
    }

    public IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    @SneakyThrows
    public String encrypt(String input, String password) {
        byte[] salt = new byte[128];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        IvParameterSpec iv = generateIv();
        String content = encrypt(
                input,
                generateKey(128, password.toCharArray(), salt),
                iv
        );

        return Base64.getEncoder().encodeToString(salt) + Base64.getEncoder().encodeToString(iv.getIV()) + content;
    }

    public String encrypt(String input, SecretKey key,
                          IvParameterSpec iv) throws InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    @SneakyThrows
    public String decrypt(String input, String password) {
        byte[] salt = Base64.getDecoder().decode(input.substring(0, 172));
        byte[] iv = Base64.getDecoder().decode(input.substring(172, 172 + 22));
        return decrypt(
                input.substring(172 + 24),
                generateKey(128, password.toCharArray(), salt),
                new IvParameterSpec(iv)
        );
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
