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
}
