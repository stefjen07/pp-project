package com.stefjen07;

import com.stefjen07.arithmetic.ArithmeticParser;
import org.junit.Assert;
import org.junit.Test;

public class ArithmeticTests {
    @Test
    public void evaluate() {
        ArithmeticParser parser = new ArithmeticParser();

        Assert.assertEquals(-3, parser.parse("-1*2-1"), 0.0001);
        Assert.assertEquals(3, parser.parse("1+2"), 0.0001);
        Assert.assertEquals(12, parser.parse("10-1+2*3*2/4"), 0.0001);
        Assert.assertEquals(220, parser.parse("(7+3)(10+1)*2"), 0.0001);
        Assert.assertEquals(-1, parser.parse("(1+2)/(3*4)(5-1)-2"), 0.0001);
        Assert.assertEquals(-30.0721649485, parser.parse("15/(7-(1+1))*3-(2+(1+1))*15/(7-(200+1))3-(2+(1+1))(15/(7-(1+1))*3-(2+(1+1))+15/(7-(1+1))*3-(2+(1+1)))"), 0.0001);
    }
}
