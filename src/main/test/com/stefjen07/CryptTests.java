package com.stefjen07;

import com.stefjen07.crypt.CryptUtil;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import static com.stefjen07.crypt.CryptPreset.aes;

public class CryptTests {
    @SneakyThrows
    @Test
    public void testEncryptionDecryptionWithoutPassword() {
        CryptUtil cryptUtil = new CryptUtil(aes);
        String input = "some text to be encrypted";
        SecretKey key = cryptUtil.generateKey();
        IvParameterSpec ivParameterSpec = cryptUtil.generateIv();
        String cipherText = cryptUtil.encrypt(input, key, ivParameterSpec);
        String plainText = cryptUtil.decrypt(cipherText, key, ivParameterSpec);
        Assert.assertEquals(input, plainText);
    }

    @SneakyThrows
    @Test
    public void testEncryptionDecryptionWithPassword() {
        CryptUtil cryptUtil = new CryptUtil(aes);
        String input = "some text to be encrypted";
        String password = "some password 123";
        String cipherText = cryptUtil.encrypt(input, password);
        String plainText = cryptUtil.decrypt(cipherText, password);
        Assert.assertEquals(input, plainText);
    }
}
