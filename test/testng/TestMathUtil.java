package testng;

import org.testng.annotations.Test;
import utils.math.MathUtil;

import static org.testng.Assert.assertEquals;

public class TestMathUtil {
    @Test
    public void TestHCF() {
        assertEquals(MathUtil.gcd(2, 4), 2);
        assertEquals(MathUtil.gcd(3, 7), 1);
    }
    
    @Test
    public void TestLCM() {
        assertEquals(MathUtil.lcm(1, 3), 3);
        assertEquals(MathUtil.lcm(3, 7), 21);
    }

    @Test
    public void testSimpleSeq() {
        assertEquals(MathUtil.sumOfSeq(3), 6);
        assertEquals(MathUtil.sumOfSeq(4), 10);
        assertEquals(MathUtil.sumOfSeq(5), 15);
        assertEquals(MathUtil.sumOfSeq(6), 21);
        assertEquals(MathUtil.sumOfSeq(7), 28);
        assertEquals(MathUtil.sumOfSeq(8), 36);
    }

    @Test
    public void testArithmatic() {
        assertEquals(MathUtil.sumOfSeq(7) - MathUtil.sumOfSeq(3), 22);
        assertEquals(MathUtil.sumOfSeq(7, 4), 22);
        assertEquals(MathUtil.sumOfSeq(4, 7), 22);
        assertEquals(MathUtil.sumOfSeq(4, 1), 10);
        assertEquals(MathUtil.sumOfSeq(3, 1), 6);
    }
}
