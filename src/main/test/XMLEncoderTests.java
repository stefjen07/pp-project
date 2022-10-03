import com.stefjen07.encoder.Encoder;
import com.stefjen07.xml.XMLEncoder;
import org.junit.Assert;
import org.junit.Test;

public class XMLEncoderTests {
    @Test
    public void encodeEmptyObject() {
        Encoder encoder = new XMLEncoder();
        String result = encoder.encode(new Object());
        Assert.assertEquals("", result);
    }

    @Test
    public void encodeInteger() {
        Integer integer = 25;

        Encoder encoder = new XMLEncoder();
        String result = encoder.encode(integer);
        Assert.assertEquals("25", result);
    }

    @Test
    public void encodeUser() {
        User user = new User();

        Encoder encoder = new XMLEncoder();
        String result = encoder.encode(user);
        Assert.assertEquals("", result);
    }
}
