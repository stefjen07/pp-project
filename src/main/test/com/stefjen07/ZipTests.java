package com.stefjen07;

import com.stefjen07.zip.ZipUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

public class ZipTests {
    @Test
    public void testZipUnzipRandomBytes() throws FileNotFoundException {
        ZipUtil zipUtil = new ZipUtil();

        byte[] someBytes = new byte[100];
        new Random().nextBytes(someBytes);

        zipUtil.zip("hello.zip", "bytes.bin", someBytes);
        Assert.assertArrayEquals(someBytes, zipUtil.unzip("hello.zip", "bytes.bin"));
    }

    @Test
    public void testZipContent() throws IOException {
        ZipUtil zipUtil = new ZipUtil();

        byte[] someBytes = new byte[100];
        new Random().nextBytes(someBytes);
        String someString = Base64.getEncoder().encodeToString(someBytes);

        String zipped = zipUtil.zip(someString, "results.txt");
        Assert.assertEquals(someString, zipUtil.unzip(zipped));
    }
}
