import com.stefjen07.decoder.Decoder;
import com.stefjen07.xml.XMLDecoder;
import org.junit.Assert;
import org.junit.Test;

public class XMLDecoderTests {
    @Test
    public void decodeInteger() {
        Decoder decoder = new XMLDecoder("25");
        Integer result = (Integer) decoder.decode(Integer.class);
        Assert.assertEquals(25, result.intValue());
    }

    @Test
    public void decodeEmptyArray() {
        Decoder decoder = new XMLDecoder("");
        Object[] result = (Object[]) decoder.decode(Object[].class);
        Object[] expected = {};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void decodeUser() {
        Decoder decoder = new XMLDecoder("<name>Eugene</name><password>123456</password>");
        User result = (User) decoder.decode(User.class);

        Assert.assertEquals("Eugene", result.name);
        Assert.assertEquals("123456", result.password);
    }

    @Test
    public void decodeArray() {
        Decoder decoder = new XMLDecoder("<User><name>Eugene</name><password>123456</password></User>");
        Object[] result = (Object[]) decoder.decode(User[].class);

        User[] expectation = {
                new User("Eugene", "123456")
        };

        Assert.assertArrayEquals(result, expectation);
    }
}
