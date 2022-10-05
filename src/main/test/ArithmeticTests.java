import com.stefjen07.arithmetic.ArithmeticParser;
import org.junit.Assert;
import org.junit.Test;

public class ArithmeticTests {
    @Test
    public void evaluate() {
        ArithmeticParser parser = new ArithmeticParser();
        Assert.assertEquals(3, parser.parse("1+2"));
    }
}
