package com.stefjen07;

import com.stefjen07.decoder.Decoder;
import com.stefjen07.json.JSONDecoder;
import org.junit.Assert;
import org.junit.Test;

public class JSONDecoderTests {
    @Test
    public void decodeInteger() {
        Decoder decoder = new JSONDecoder("25");
        Integer result = (Integer) decoder.decode(Integer.class);
        Assert.assertEquals(25, result.intValue());
    }

    @Test
    public void decodeEmptyArray() {
        Decoder decoder = new JSONDecoder("");
        Object[] result = (Object[]) decoder.decode(Object[].class);
        Object[] expected = {};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void decodeUser() {
        Decoder decoder = new JSONDecoder("{name:\"Eugene\", password: \"123456\"}");
        User result = (User) decoder.decode(User.class);

        Assert.assertEquals("Eugene", result.name);
        Assert.assertEquals("123456", result.password);
    }

    @Test
    public void decodeArray() {
        Decoder decoder = new JSONDecoder("[{name:\"Eugene\", password: \"123456\"}, {name:\"Eugene\", password: \"123456\"}]");
        Object[] result = (Object[]) decoder.decode(User[].class);

        User[] expectation = {
                new User("Eugene", "123456"),
                new User("Eugene", "123456")
        };

        Assert.assertArrayEquals(expectation, result);
    }
}
