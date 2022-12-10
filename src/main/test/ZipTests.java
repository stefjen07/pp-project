import com.stefjen07.zip.ZipUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class ZipTests {
    @Test
    public void testZipUnzipRandomBytes() {
        ZipUtil zipUtil = new ZipUtil();

        byte[] someBytes = new byte[100];
        new Random().nextBytes(someBytes);

        zipUtil.zip("hello.zip", "bytes.bin", someBytes);
        Assert.assertArrayEquals(someBytes, zipUtil.unzip("hello.zip", "bytes.bin"));
    }
}
