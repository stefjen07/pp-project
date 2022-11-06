import com.stefjen07.encoder.Encoder;
import com.stefjen07.xml.XMLEncoder;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
        User user = new User("Eugene", "123456");

        Encoder encoder = new XMLEncoder();
        String result = encoder.encode(user);
        Assert.assertEquals("<name>Eugene</name><password>123456</password>", result);
    }

    @Test
    public void encodeArray() {
        List<User> users = new ArrayList<>();
        users.add(new User("Eugene", "123456"));

        Encoder encoder = new XMLEncoder();
        String result = encoder.encode(users);
        Assert.assertEquals("<User><name>Eugene</name><password>123456</password></User>", result);
    }
}
