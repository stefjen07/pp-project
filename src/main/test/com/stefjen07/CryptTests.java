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
    public void testEncryptionDecryption() {
        CryptUtil cryptUtil = new CryptUtil(aes);
        String input = "some text to be encrypted";
        SecretKey key = cryptUtil.generateKey();
        IvParameterSpec ivParameterSpec = cryptUtil.generateIv();
        String cipherText = cryptUtil.encrypt(input, key, ivParameterSpec);
        String plainText = cryptUtil.decrypt(cipherText, key, ivParameterSpec);
        Assert.assertEquals(input, plainText);
    }
}
