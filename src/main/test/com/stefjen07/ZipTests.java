package com.stefjen07;

import com.stefjen07.zip.ZipUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Base64;
import java.util.Random;

public class ZipTests {
    @Test
    public void testZipContent() {
        ZipUtil zipUtil = new ZipUtil();

        byte[] someBytes = new byte[100];
        new Random().nextBytes(someBytes);
        String someString = Base64.getEncoder().encodeToString(someBytes);

        String zipped = zipUtil.zip(someString, "results.txt");
        Assert.assertEquals(someString, zipUtil.unzip(zipped, "txt"));
    }
}
